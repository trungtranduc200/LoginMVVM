package com.example.loginmvvm.view

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
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
    }

    private fun initData() {
        pref = getSharedPreferences("PREF", MODE_PRIVATE)
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
        viewModel.categoriesResult.observe(this) {
//            if (it.isNotEmpty()) {
//                categoriesAdapter.setData(it)
//                binding.actCategoriesRclCategories.layoutManager = GridLayoutManager(this, 3)
//                binding.actCategoriesRclCategories.adapter = categoriesAdapter
//            }
            categoriesAdapter.setData(dataList())
            binding.actCategoriesRclCategories.layoutManager = GridLayoutManager(this, 3)
            binding.actCategoriesRclCategories.adapter = categoriesAdapter

        }

        binding.actCategoriesBtnBack.setOnClickListener {
            finish()
        }

        binding.actCategoriesTvDone.setOnClickListener {
            Toast.makeText(this,"Vào màn hình bên trong",Toast.LENGTH_SHORT).show()
        }
    }

    private fun dataList(): List<ItemModel> {
        return mutableListOf(
            ItemModel(1, "23/09/2023", "23/09/2023", "Hopes"),
            ItemModel(1, "23/09/2023", "23/09/2023", "Bullying"),
            ItemModel(1, "23/09/2023", "23/09/2023", "Works"),
            ItemModel(1, "23/09/2023", "23/09/2023", "Music"),
            ItemModel(1, "23/09/2023", "23/09/2023", "Hopes"),
            ItemModel(1, "23/09/2023", "23/09/2023", "Hopes"),
            ItemModel(1, "23/09/2023", "23/09/2023", "Hopes"),
            ItemModel(1, "23/09/2023", "23/09/2023", "Bullying"),
        )
    }

    override fun onClick(itemModel: ItemModel, list: List<ItemModel>) {
        var enableButtonDone = list.any{
            it.selected
        }
        binding.actCategoriesTvDone.isEnabled = enableButtonDone
        Toast.makeText(this, itemModel.name, Toast.LENGTH_SHORT).show()
    }
}