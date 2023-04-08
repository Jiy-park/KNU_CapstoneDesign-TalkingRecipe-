package com.example.capstone_recipe.create_fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import com.example.capstone_recipe.R
import com.example.capstone_recipe.data_class.LEVEL
import com.example.capstone_recipe.data_class.RecipeBasicInfo
import com.example.capstone_recipe.data_class.RecipeIngredient
import com.example.capstone_recipe.databinding.FragmentRecipeCreateStepFirstBinding

class RecipeCreateStepFirst(_ingredientList: MutableList<RecipeIngredient>, _recipeBasicInfo: RecipeBasicInfo) : Fragment() {
    private lateinit var binding:FragmentRecipeCreateStepFirstBinding
    private lateinit var context:Context
    private val ingredientIdList = mutableListOf<Int>() // 재료 추가 뷰 아이디 저장용 리스트
    private var ingredientList = _ingredientList    // 재료 리스트
    private var recipeBasicInfo = _recipeBasicInfo  // 레시피 기본 정보

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRecipeCreateStepFirstBinding.inflate(inflater, container, false)
        context = binding.root.context

        addIngredientLayer() // 첫번째 재료

        binding.rgLevelSelection.setOnCheckedChangeListener { _, checkedId -> // 라디오 버튼 텍스트 변경
            setRadioTextColor(checkedId, context) //checkedId에 해당하는 라디오 버튼 텍스트만 색 변경 및 난이도 저장
        }

        setEditChangeDetect(binding.editCreateTitle, TARGET.TITLE)
        setEditChangeDetect(binding.editCreateIntro, TARGET.INTRO)
        setEditChangeDetect(binding.layerQuestionTime.editAnswer, TARGET.TIME)
        setEditChangeDetect(binding.layerQuestionAmount.editAnswer, TARGET.AMOUNT)

        binding.layerQuestionAmount.tvQuestion.text = "양은 얼마나 되나요?"
        binding.layerQuestionAmount.tvUnit.text = "인분"

        return binding.root
    }

    @SuppressLint("InflateParams")
    private fun addIngredientLayer(){ // 재료 추가 뷰 생성 함수
        val addView = LayoutInflater.from(context).inflate(R.layout.common_recipe_ingredient, null)
        addView.id = View.generateViewId()
        addView.requestFocus()

        addView.findViewById<ImageButton>(R.id.btnAddIngredient).setOnClickListener { addIngredientLayer() }       // 재료 추가
        addView.findViewById<ImageButton>(R.id.btnRemoveIngredient).setOnClickListener {                    // 재료 제거
            val parentViewId = (it.parent as ViewGroup).id
            removeIngredientLayer(parentViewId)
        }
        addView.findViewById<EditText>(R.id.editIngredientName).addTextChangedListener(object: TextWatcher{         // 재료 이름
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val position = ingredientIdList.indexOf(addView.id)
                ingredientList[position].name = addView.findViewById<EditText>(R.id.editIngredientName).text.toString()
            }
        })
        addView.findViewById<EditText>(R.id.editIngredientAmount).addTextChangedListener(object: TextWatcher{       // 재료 양
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val position = ingredientIdList.indexOf(addView.id)
                ingredientList[position].amount = addView.findViewById<EditText>(R.id.editIngredientAmount).text.toString()
            }
        })

        binding.linearForIngredient.addView(addView)

        ingredientIdList.add(addView.id)
        ingredientList.add(RecipeIngredient("", ""))


        if(ingredientIdList.size == 1){
            val viewId = ingredientIdList[0]
            val view = binding.root.findViewById<View>(viewId)
            view.findViewById<ImageButton>(R.id.btnRemoveIngredient).visibility = View.INVISIBLE
        }
        else{
            val index = ingredientIdList.size - 2
            val viewId = ingredientIdList[index]
            val view = binding.root.findViewById<View>(viewId)
            view.findViewById<ImageButton>(R.id.btnAddIngredient).visibility = View.INVISIBLE
        }
    }

    private fun removeIngredientLayer(prentViewId:Int){ //재료 추가 뷰 삭제 함수
        val view = binding.root.findViewById<View>(prentViewId)
        binding.linearForIngredient.removeView(view)
        val position = ingredientIdList.indexOf(prentViewId)
        ingredientIdList.removeAt(position)
        ingredientList.removeAt(position)

        Log.d("LOG_CHECK", "id list : $ingredientIdList , ingredient list : $ingredientList")

        if(position == ingredientIdList.size){
            val lastViewId =  ingredientIdList[position-1]
            val lastView = binding.root.findViewById<View>(lastViewId)
            lastView.findViewById<ImageButton>(R.id.btnAddIngredient).visibility = View.VISIBLE
        }
    }

    private enum class TARGET{ TITLE, INTRO, TIME, AMOUNT }
    private fun setEditChangeDetect(origin:EditText, target:TARGET){
        origin.addTextChangedListener(object : TextWatcher{ // 리세피 제목 저장
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                when(target){
                    TARGET.TITLE -> recipeBasicInfo.title = origin.text.toString()     // 레시피 제목 저장
                    TARGET.INTRO -> recipeBasicInfo.intro = origin.text.toString()     // 레시피 한 줄 소개 저장
                    TARGET.TIME -> recipeBasicInfo.time = origin.text.toString()      // 레시피 소요 시간 저장
                    TARGET.AMOUNT -> recipeBasicInfo.amount = origin.text.toString()    // 레시피 음식 양 저장
                }
            }
        })
    }

    private fun setRadioTextColor(targetId:Int, context:Context){ // 라디오 버튼 텍스트 색 변경 함수
        val radioLevel = listOf<Int>(
            R.id.radioLevelEasy,
            R.id.radioLevelNormal,
            R.id.radioLevelHard
        )

        for(i in radioLevel.indices){
            binding.root.findViewById<RadioButton>(radioLevel[i]).setTextColor(ContextCompat.getColor(context, R.color.main_text))
            if(radioLevel[i] == targetId){
                binding.root.findViewById<RadioButton>(radioLevel[i]).setTextColor(ContextCompat.getColor(context, R.color.main_color_start))
                recipeBasicInfo.level = LEVEL.values()[i] // 레시피 난이도 저장
            }
        }
    }
}