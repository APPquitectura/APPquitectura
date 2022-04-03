package com.etsisi.appquitectura.presentation.ui.main.view

import android.os.CountDownTimer
import androidx.navigation.fragment.navArgs
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentPlayBinding
import com.etsisi.appquitectura.databinding.ItemTabHeaderBinding
import com.etsisi.appquitectura.domain.enums.GameNavType
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.etsisi.appquitectura.presentation.common.PlayFragmentListener
import com.etsisi.appquitectura.presentation.ui.main.adapter.QuestionsViewPagerAdapter
import com.etsisi.appquitectura.presentation.ui.main.model.ItemGameMode
import com.etsisi.appquitectura.presentation.ui.main.viewmodel.PlayViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class PlayFragment : BaseFragment<FragmentPlayBinding, PlayViewModel>(
    R.layout.fragment_play,
    PlayViewModel::class
), PlayFragmentListener, TabLayout.OnTabSelectedListener {

    val args: PlayFragmentArgs by navArgs()
    private val questionsAdapter by lazy { QuestionsViewPagerAdapter(this) }
    private val readySetGoCounter by lazy { object : CountDownTimer(4000, 1000) {
        override fun onTick(millisUntilFinished: Long) {}
        override fun onFinish() {
            mViewModel.setNavType(GameNavType.START_GAME)
        }
    } }

    companion object {
        private const val SELECTED_ALPHA = 1.0F
        private const val UNSELECTED_ALPHA = 0.2F
    }

    override fun setUpDataBinding(mBinding: FragmentPlayBinding, mViewModel: PlayViewModel) {
        with(mBinding) {
            lifecycleOwner = viewLifecycleOwner
            lifecycle.addObserver(mViewModel)
            viewModel = mViewModel
            mViewModel.setNavType(args.navType)
            if (args.navType == GameNavType.PRE_START_GAME) {
                readySetGoCounter.start().also {
                    readyToStartGame.playAnimation()
                }
            }
            listener = this@PlayFragment
            viewPager.adapter = questionsAdapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.customView = ItemTabHeaderBinding.inflate(layoutInflater, tab.view, false)
                    .apply {
                        questionPosition.text = position.toString()
                    }.root
                setTabAlpha(tab, false)
            }.attach()
            tabLayout.apply {
                setSelectedTabIndicator(null)
                addOnTabSelectedListener(this@PlayFragment)
            }
            executePendingBindings()
        }
    }

    override fun observeViewModel(mViewModel: PlayViewModel) {
        with(mViewModel) {
            navType.observe(viewLifecycleOwner) {
                if (it == GameNavType.PRE_START_GAME) {
                    fetchInitialQuestions(args.gameMode)
                }
            }
            questions.observe(viewLifecycleOwner) {
                questionsAdapter.addData(it, mBinding.tabLayout.selectedTabPosition)
            }
        }
    }

    override fun onGameMode(item: ItemGameMode) {
        navigator.startGame(item.action)
    }

    override fun onTabSelected(tab: TabLayout.Tab?) = setTabAlpha(tab, true)

    override fun onTabUnselected(tab: TabLayout.Tab?) = setTabAlpha(tab, false)

    override fun onTabReselected(tab: TabLayout.Tab?) = setTabAlpha(tab, true)

    private fun setTabAlpha(tab: TabLayout.Tab?, selected: Boolean) {
        tab?.customView?.alpha = if (selected) SELECTED_ALPHA else UNSELECTED_ALPHA
    }

}