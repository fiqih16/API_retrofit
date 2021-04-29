package com.fiqih.api

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupListOfDataIntoRecyclerView()
        btnR.setOnClickListener{
            addRecord()
        }
        }

    fun setupListOfDataIntoRecyclerView() {
        rv_item.layoutManager = LinearLayoutManager(this)


    // Ambil data CEO dari API
    var apiInterface: ApiInterface = ApiClient().getApiClient()!!.create(ApiInterface::class.java)
    apiInterface.getCEOs().enqueue(object : retrofit2.Callback<ArrayList<CEOModel>> {


        override fun onFailure(call: Call<ArrayList<CEOModel>>?, t: Throwable) {
            Toast.makeText(baseContext, "Data downloading is failed", Toast.LENGTH_LONG).show()
        }

        override fun onResponse(call: Call<ArrayList<CEOModel>>?, response: Response<ArrayList<CEOModel>>?) {
            var ceoData = response?.body()!!
            if (ceoData.size > 0) {
                rv_item.visibility = View.VISIBLE
                TV_No_record.visibility = View.GONE
                rv_item.adapter = MyAdapter(this@MainActivity, ceoData)
            } else {
                rv_item.visibility = View.GONE
                TV_No_record.visibility = View.VISIBLE
            }
            Toast.makeText(baseContext,"Data download success", Toast.LENGTH_LONG).show()

        }
    })

    }

    //Method Tambah data
    fun addRecord() {
        val nama = etNama.text.toString()
        val namapt = etPT.text.toString()

        if (nama == "" || namapt == ""){
            Toast.makeText(this,
                "Masih ada kolom yang kosong", Toast.LENGTH_LONG).show()
        }else{
            val newCEO : CEOModel = CEOModel(null, nama, namapt)

            var apiInterface: ApiInterface = ApiClient().getApiClient()!!.create(ApiInterface::class.java)
            var requestCall : Call<CEOModel> = apiInterface.addCEO(newCEO)

            requestCall.enqueue(object : Callback<CEOModel>{

                override fun onResponse(call: Call<CEOModel>, response: Response<CEOModel>) {
                    if(response.isSuccessful){
                        Toast.makeText(this@MainActivity,
                            "Berhasil tersimpan", Toast.LENGTH_LONG).show()
                        setupListOfDataIntoRecyclerView()
                        etNama.setText("")
                        etPT.setText("")
                    }else{
                        Toast.makeText(this@MainActivity,
                            "Gagal tersimpan", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<CEOModel>, t: Throwable) {
                    Toast.makeText(this@MainActivity,
                        "Gagal tersimpan", Toast.LENGTH_LONG).show()

                }

            })
        }
    }

    // Untuk menampilkan dialog delete
    fun deleteRecordDialog(CEOModel: CEOModel?){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Berhasil Dihapus")

        builder.setMessage("Apa Kamu Yakin?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Ya"){dialog, which: Int ->
            var apiInterface: ApiInterface =
                ApiClient().getApiClient()!!.create(ApiInterface::class.java)

            var requestCall: Call<CEOModel> = apiInterface.deleteCEO(CEOModel?.id!!)
            requestCall.enqueue(object : Callback<CEOModel>{
                override fun onResponse(call: Call<CEOModel>, response: Response<CEOModel>) {
                    if(response.isSuccessful){
                        Toast.makeText(this@MainActivity,
                            "Berhasil terhapus", Toast.LENGTH_LONG).show()
                        setupListOfDataIntoRecyclerView()
                    }
                }

                override fun onFailure(call: Call<CEOModel>, t: Throwable) {
                    Toast.makeText(this@MainActivity,
                        "Gagal Terhapus", Toast.LENGTH_LONG).show()
                }

            })
        }


        builder.setNegativeButton("No"){dialog, which: Int->
            dialog?.dismiss()
        }
        builder.show()
    }

    fun updateRecordDialog(CEOModel: CEOModel?){

    }

}
