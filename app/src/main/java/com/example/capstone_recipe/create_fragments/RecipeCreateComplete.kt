package com.example.capstone_recipe.create_fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.capstone_recipe.RecipeCreate
import com.example.capstone_recipe.PostViewer
import com.example.capstone_recipe.data_class.RecipeBasicInfo
import com.example.capstone_recipe.data_class.RecipeStep
import com.example.capstone_recipe.databinding.FragmentRecipeCreateCompleteBinding
import com.google.firebase.storage.FirebaseStorage

class RecipeCreateComplete() : Fragment() {
    private lateinit var binding: FragmentRecipeCreateCompleteBinding
    private lateinit var context: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRecipeCreateCompleteBinding.inflate(inflater, container, false)
        context = binding.root.context

        binding.btnReturnMain.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            requireActivity().finish()
            Toast.makeText(context, "${requireActivity().supportFragmentManager.backStackEntryCount}", Toast.LENGTH_SHORT).show()
        }
        binding.btnCheckPost.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            requireActivity().finish()
            val intent = Intent(context, PostViewer::class.java)
            intent.putExtra("recipeID",1) // 포스트 고유 id값
            startActivity(intent)
        }

        return binding.root
    }


}