package com.example.loginmvvm.view

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.loginmvvm.R
import com.example.loginmvvm.adapter.CategoriesAdapter
import com.example.loginmvvm.databinding.ActivityCategoriesBinding
import com.example.loginmvvm.factory.CategoriesViewModelFactory
import com.example.loginmvvm.model.ItemModel
import com.example.loginmvvm.repository.CategoriesRepository
import com.example.loginmvvm.service.APIService
import com.example.loginmvvm.util.Constant
import com.example.loginmvvm.util.Constant.KEY_SAVE_LIST
import com.example.loginmvvm.viewmodel.CategoriesViewModel
import com.google.gson.Gson

class CategoriesActivity : AppCompatActivity(), CategoriesAdapter.OnClickItem {
    private lateinit var binding: ActivityCategoriesBinding
    private lateinit var viewModel: CategoriesViewModel
    private lateinit var pref :SharedPreferences
    private lateinit var editor: Editor
    private val categoriesAdapter by lazy {
        CategoriesAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingView()
    }

    private fun bindingView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_categories)
        binding.lifecycleOwner = this
        val apiService = APIService.service
        val repository = CategoriesRepository(apiService)
        val viewModelFactory = CategoriesViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[CategoriesViewModel::class.java]
        binding.viewModel = viewModel
        initData()
        initView()
        callback()
    }

    private fun initData() {
        pref = getSharedPreferences(Constant.KEY_PREF, MODE_PRIVATE)
        editor = pref.edit()
        val token = pref.getString(Constant.KEY_TOKEN_LOGIN, null)
        if (token != null) {
            if (token.isNotEmpty()) {
                viewModel.getListCategory(token)
            } else {
                viewModel.getListCategory("")
            }
        }
    }

    private fun initView() {
        binding.actCategoriesTvDone.isEnabled = false


        binding.actCategoriesBtnBack.setOnClickListener {
            finish()
        }

        binding.actCategoriesTvDone.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun callback() {
        viewModel.categoriesResult.observe(this) {
            if (it.isNotEmpty()&&it != null) {
                categoriesAdapter.setData(it)
                binding.actCategoriesRclCategories.layoutManager = GridLayoutManager(this, 3)
                binding.actCategoriesRclCategories.adapter = categoriesAdapter
            }
        }

        viewModel.categoriesError.observe(this){
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(itemModel: ItemModel, list: List<ItemModel>) {
        val enableButtonDone = list.any{
            it.selected
        }
        saveDataLocal(list)
        binding.actCategoriesTvDone.isEnabled = enableButtonDone
        Toast.makeText(this, itemModel.name, Toast.LENGTH_SHORT).show()
    }

    private fun saveDataLocal(list: List<ItemModel>){
        val result = list.filter {
            it.selected
        }
        val pref = getSharedPreferences(Constant.KEY_PREF_LIST, MODE_PRIVATE)
        val editor = pref.edit()
        val gson = Gson()
        val json = gson.toJson(result)
        editor.putString(KEY_SAVE_LIST,json)
        editor.apply()


        //View log data save json List
        val data = pref.getString(KEY_SAVE_LIST, null)
        Log.d("trungtd11",data.toString())
    }
}