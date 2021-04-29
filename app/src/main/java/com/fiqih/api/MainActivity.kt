package com.fiqih.api

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.update_dialog.*
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
            closeKeyboard()
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

    // Method to show custome dialog
    fun updateRecordDialog(CEOModel: CEOModel?){
        val updateDialog = Dialog(this,R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        updateDialog.setContentView(R.layout.update_dialog)

        updateDialog.etUpName.setText(CEOModel?.name)
        updateDialog.etUpNamePt.setText(CEOModel?.company_name)

        updateDialog.tvUpdate.setOnClickListener {
            val name = updateDialog.etUpName.text.toString()
            val companyName = updateDialog.etUpNamePt.text.toString()

            if(name.isEmpty() && companyName.isEmpty()){
                Toast.makeText(this,
                "Masih Ada Field yang kosong, tolong lengkapi",
                Toast.LENGTH_LONG).show()
            }else{
                val newCEO : CEOModel = CEOModel(null, name, companyName)

                var apiInterface: ApiInterface = ApiClient().getApiClient()!!.create(ApiInterface::class.java)
                var requestCall : Call<CEOModel> = apiInterface.updateCEO(newCEO, CEOModel?.id!!)

                requestCall.enqueue(object : Callback<CEOModel>{

                    override fun onResponse(call: Call<CEOModel>, response: Response<CEOModel>) {
                        if(response.isSuccessful){
                            Toast.makeText(this@MainActivity,
                                "Berhasil tersimpan", Toast.LENGTH_LONG).show()
                            setupListOfDataIntoRecyclerView()
                            etNama.setText("")
                            etPT.setText("")
                            updateDialog.dismiss()
                            closeKeyboard()
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
        updateDialog.tvCancel.setOnClickListener{
            updateDialog.dismiss()
        }
        updateDialog.show()

    }

    // method to close keyboard
    private fun closeKeyboard(){
        val view = this.currentFocus
        if (view!= null){
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken,0)
        }
    }

}
