package com.facile.immediate.electronique.fleurs.pret.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.facile.immediate.electronique.fleurs.pret.databinding.ViewDividingRulerBinding

class DividingRulerView @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributes, defStyleAttr) {

    private val binding: ViewDividingRulerBinding

    init {
        binding = ViewDividingRulerBinding.inflate(LayoutInflater.from(context), this)
    }

    fun setCurMaxMount(maxMount: String) {
        try {
            val maxMountInt: Int = maxMount.toInt()
            binding.inDividingLeft.tvOffset1.text = "${maxMountInt - 1000}"
            binding.inDividingLeft.tvOffset2.text = "${maxMountInt - 2000}"

            binding.inDividingRight.tvOffset1.text = "${maxMountInt - 1000}"
            binding.inDividingRight.tvOffset2.text = "${maxMountInt - 2000}"
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}