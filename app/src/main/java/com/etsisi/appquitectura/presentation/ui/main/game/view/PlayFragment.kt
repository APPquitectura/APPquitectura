package com.etsisi.appquitectura.presentation.ui.main.game.view

import android.content.Context
import android.os.CountDownTimer
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentPlayBinding
import com.etsisi.appquitectura.databinding.ItemTabHeaderBinding
import com.etsisi.appquitectura.domain.enums.GameNavType
import com.etsisi.appquitectura.domain.model.AnswerBO
import com.etsisi.appquitectura.domain.model.QuestionBO
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.etsisi.appquitectura.presentation.common.GameListener
import com.etsisi.appquitectura.presentation.common.LiveEventObserver
import com.etsisi.appquitectura.presentation.components.ZoomOutPageTransformer
import com.etsisi.appquitectura.presentation.dialog.enums.DialogType
import com.etsisi.appquitectura.presentation.dialog.model.DialogConfig
import com.etsisi.appquitectura.presentation.dialog.view.TopicPickerDialog
import com.etsisi.appquitectura.presentation.ui.main.game.adapter.GameModeAdapter
import com.etsisi.appquitectura.presentation.ui.main.game.adapter.QuestionsViewPagerAdapter
import com.etsisi.appquitectura.domain.enums.GameType
import com.etsisi.appquitectura.domain.enums.QuestionLevel
import com.etsisi.appquitectura.presentation.ui.main.game.viewmodel.PlayViewModel
import com.etsisi.appquitectura.presentation.utils.TAG
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class PlayFragment : BaseFragment<FragmentPlayBinding, PlayViewModel>(
    R.layout.fragment_play,
    PlayViewModel::class
), TabLayout.OnTabSelectedListener, GameListener {

    private val args: PlayFragmentArgs by navArgs()

    private val questionsAdapter: QuestionsViewPagerAdapter?
        get() = questionsViewPager.adapter as? QuestionsViewPagerAdapter

    private val gameModesAdapter: GameModeAdapter?
        get() = mBinding.gameModesRv.adapter as? GameModeAdapter

    private val questionsViewPager: ViewPager2
        get() = mBinding.viewPager

    private val initialNavType: GameNavType
        get() = args.navType
    private val questionsToRepeat: List<QuestionBO>?
        get() = args.incorrectQuestions?.toList()

    private val readySetGoCounter by lazy {
        object : CountDownTimer(READY_SET_GO_COUNT_DOWN, READY_SET_GO_COUNTER_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                mViewModel.setNavType(GameNavType.START_GAME)
            }
        }
    }

    private companion object {
        const val READY_SET_GO_COUNTER_INTERVAL = 1000L
        const val READY_SET_GO_COUNT_DOWN = 4000L
        const val NEXT_QUESTION_DELAY = 300L
        const val SELECTED_ALPHA = 1.0F
        const val UNSELECTED_ALPHA = 0.2F
    }

    override fun getFragmentArgs(mBinding: FragmentPlayBinding) {
        with(mViewModel) {
            setGameModeSelected(args.gameModeIndex)
            setTopicsSelected(args.topicsIdSelected?.toList())
            setLevelSelected(args.levelSelected)
            setNavType(initialNavType)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        with(requireActivity()) {
            onBackPressedDispatcher.apply {
                addCallback(this@PlayFragment, object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (initialNavType == GameNavType.GAME_MODE) {
                            navigator.onBackPressed()
                        } else {
                            navigator.openNavigationDialog(
                                type = DialogType.WARNING_LEAVING_GAME,
                                config = DialogConfig(title = R.string.dialog_leaving_game_title, body = R.string.dialog_leaving_game_body)
                            )
                        }
                        this.remove()
                        isEnabled = false
                    }
                })
            }
        }
    }

    override fun setUpDataBinding(mBinding: FragmentPlayBinding, mViewModel: PlayViewModel) {
        with(mBinding) {
            lifecycleOwner = viewLifecycleOwner
            lifecycle.addObserver(mViewModel)
            viewModel = mViewModel

            gameModesRv.adapter = GameModeAdapter(this@PlayFragment)

            viewPager.apply {
                adapter = QuestionsViewPagerAdapter(this@PlayFragment)
                isUserInputEnabled = false
                setPageTransformer(ZoomOutPageTransformer())
            }
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.customView = ItemTabHeaderBinding.inflate(layoutInflater, tab.view, false)
                    .apply {
                        lifecycleOwner = viewLifecycleOwner
                        questionIndex = position + 1
                        viewModel = mViewModel
                    }.root
                tab.view.setOnTouchListener { v, event -> true }
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
                with(mBinding) {
                    when(it) {
                        GameNavType.PRE_START_GAME -> {
                            hideSystemBars()
                            readySetGoCounter.start().also { readyToStartGame.playAnimation() }
                            fetchInitialQuestions()
                        }
                        GameNavType.REPEAT_INCORRECT_ANSWERS -> {
                            hideSystemBars()
                            readySetGoCounter.start().also { readyToStartGame.playAnimation() }
                            questionsToRepeat?.let { setQuestions(it) }
                        }
                        GameNavType.GAME_MODE -> {
                            getGameModes()
                        }
                        else -> {

                        }
                    }
                }
            }
            questionsLoaded.observe(viewLifecycleOwner) {
                if (it) {
                    setInitialQuestions()
                }
            }
            questions.observe(viewLifecycleOwner) {
                questionsAdapter?.addData(it, mBinding.tabLayout.selectedTabPosition.takeIf { it > -1 } ?: 0)
            }
            gameModes.observe(viewLifecycleOwner) {
                gameModesAdapter?.addDataSet(it)
            }
            onShowTopicPicker.observe(viewLifecycleOwner, LiveEventObserver {
                TopicPickerDialog.newInstance(
                    it,
                    getGameModes().indexOfFirst { it.action is GameType.TestGame },
                    this@PlayFragment
                ).show(childFragmentManager, TopicPickerDialog.TAG)
            })
            startGame.observe(viewLifecycleOwner, LiveEventObserver {
                navigator.startGame(it, _labelsSelectedIndex, _levelSelected ?: QuestionLevel.UNKNOWN)
            })
            repeatIncorrectAnswers.observe(viewLifecycleOwner, LiveEventObserver {
                navigator.repeatIncorrectAnswers(it)
            })
            showResults.observe(viewLifecycleOwner, LiveEventObserver {
                navigator.openResultFragment()
            })
            showError.observe(viewLifecycleOwner, LiveEventObserver {
                navigator.openNavigationDialog(
                    config = it,
                    type = DialogType.NO_QUESTIONS_FOUND
                )
            })
        }
    }

    override fun onTabSelected(tab: TabLayout.Tab?) = setTabAlpha(tab, true)

    override fun onTabUnselected(tab: TabLayout.Tab?) = setTabAlpha(tab, false)

    override fun onTabReselected(tab: TabLayout.Tab?) = setTabAlpha(tab, true)

    private fun setTabAlpha(tab: TabLayout.Tab?, selected: Boolean) {
        if (selected) {
            mViewModel.setCurrentTabIndex(tab?.position?.plus(1) ?: 1)
        }
        tab?.customView?.alpha = if (selected) SELECTED_ALPHA else UNSELECTED_ALPHA
    }

    override fun onAnswerClicked(question: QuestionBO, answer: AnswerBO, points: Long, userMarkInMillis: Long) {
        with(questionsViewPager) {
            mViewModel.setGameResultAccumulated(
                question = question,
                userAnswer = answer,
                points = points,
                userMarkInMillis = userMarkInMillis,
                isGameFinished = currentItem == adapter?.itemCount?.minus(1),
                initialNavType = initialNavType
            ) {
                postDelayed({
                    setCurrentItem(currentItem + 1, true)
                }, NEXT_QUESTION_DELAY)
            }
        }
    }

    override fun onGameModeSelected(gameModeIndex: Int, topicsIdSelected: IntArray?, levelSelected: QuestionLevel?) {
        mViewModel.handleGameModeSelected(gameModeIndex, topicsIdSelected, levelSelected)
    }

}