package com.etsisi.appquitectura.presentation.dialog.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.DialogPickerTopicBinding
import com.etsisi.appquitectura.databinding.ItemLabelBinding
import com.etsisi.appquitectura.presentation.common.GameListener
import com.etsisi.appquitectura.presentation.ui.main.game.model.ItemLabel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip

class TopicPickerDialog(
    private val listener: GameListener,
    private val gameModeIndex: Int,
    private val items: List<ItemLabel>
): BottomSheetDialogFragment() {
    private lateinit var mBinding: DialogPickerTopicBinding


    companion object {
        fun newInstance(topics: List<ItemLabel>, gameModeIndex: Int, listener: GameListener) = TopicPickerDialog(listener, gameModeIndex, topics)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Widget_AppQuitectura_BottomSheetDialog)
        val view = inflater.inflate(R.layout.dialog_picker_topic, container, false)
        setUpDataBinding(view)
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener {
                val layout: FrameLayout? = (dialog as BottomSheetDialog).findViewById(com.google.android.material.R.id.design_bottom_sheet)
                layout?.let {
                    val behavior = BottomSheetBehavior.from(it)
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    behavior.skipCollapsed = true
                }
            }
        }
    }

    private fun setUpDataBinding(binding: View) {
        mBinding = DialogPickerTopicBinding.bind(binding)
        mBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            labels.apply {
                items.mapIndexed { index, itemLabel ->
                    val view = (ItemLabelBinding.inflate(layoutInflater, this, false).apply {
                        this.label = itemLabel
                    }.root as Chip).apply {
                        id = index
                    }
                    addView(view , index)
                }
            }
            startGame.setOnClickListener {
                listener.onGameModeSelected(gameModeIndex, labels.checkedChipIds.toIntArray().takeIf { it.isNotEmpty() })
            }
        }
    }
}