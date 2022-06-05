package com.etsisi.appquitectura.presentation.ui.login.viewmodel

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.etsisi.analytics.IFirebaseAnalytics
import com.etsisi.analytics.enums.SignUpType
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.data.model.enums.UserGender
import com.etsisi.appquitectura.domain.enums.QuestionSubject
import com.etsisi.appquitectura.domain.model.CurrentUser
import com.etsisi.appquitectura.domain.usecase.SignInWithCredentialsUseCase
import com.etsisi.appquitectura.domain.usecase.LogOutUseCase
import com.etsisi.appquitectura.domain.usecase.RegisterUseCase
import com.etsisi.appquitectura.domain.usecase.SendEmailVerificationUseCase
import com.etsisi.appquitectura.presentation.common.Event
import com.etsisi.appquitectura.presentation.common.LiveEvent
import com.etsisi.appquitectura.presentation.common.MutableLiveEvent
import com.etsisi.appquitectura.presentation.dialog.model.DialogConfig
import com.etsisi.appquitectura.presentation.ui.login.enums.RegisterError

class RegisterViewModel(
    logOutUseCase: LogOutUseCase,
    signInWithCredentialsUseCase: SignInWithCredentialsUseCase,
    sendEmailVerificationUseCase: SendEmailVerificationUseCase,
    private val analytics: IFirebaseAnalytics,
    private val registerUseCase: RegisterUseCase
) : BaseLoginViewModel(
    analytics,
    logOutUseCase,
    signInWithCredentialsUseCase,
    sendEmailVerificationUseCase
) {

    private val _loaded by lazy { MutableLiveData<Boolean>() }
    val loaded: LiveData<Boolean>
        get() = _loaded

    private val _onSuccessRegister by lazy { MutableLiveEvent<Boolean>() }
    val onSuccessRegister: LiveEvent<Boolean>
        get() = _onSuccessRegister

    private val _name = MutableLiveData<String>()
    val name: MutableLiveData<String>
        get() = _name

    private val _surname = MutableLiveData<String>()
    val surname: MutableLiveData<String>
        get() = _surname

    private val _academicRecord = MutableLiveData<String>()
    val academicRecord: MutableLiveData<String>
        get() = _academicRecord

    private val _academicGroup = MutableLiveData<String>()
    val academicGroup: MutableLiveData<String>
        get() = _academicGroup

    private val _city = MutableLiveData<String>()
    val city: MutableLiveData<String>
        get() = _city

    private val _age = MutableLiveData<String>()
    val age: LiveData<String>
        get() = _age

    private val _inputDataError = MutableLiveEvent<RegisterError>()
    val inputDataError: LiveEvent<RegisterError>
        get() = _inputDataError

    val genderOptions: List<UserGender> = listOf(UserGender.MALE, UserGender.FEMALE)
    val courseOptions: List<QuestionSubject> =
        listOf(QuestionSubject.COMPOSICION, QuestionSubject.INTRODUCCION)

    private var courseSelected = courseOptions.first()
    private var genderSelected = genderOptions.first()

    fun initRegister() {
        if (allValuesAreFilled()) {
            registerUseCase.invoke(
                scope = viewModelScope,
                params = RegisterUseCase.Params(
                    name = _name.value.orEmpty(),
                    email = _email.value.orEmpty(),
                    password = _password.value.orEmpty(),
                    course = courseSelected!!,
                    gender = genderSelected,
                    surname = _surname.value.orEmpty(),
                    city = _city.value.orEmpty(),
                    academicGroup = _academicGroup.value.orEmpty(),
                    academicRecord = _academicRecord.value.orEmpty()
                )
            ) { resultCode ->
                when (resultCode) {
                    RegisterUseCase.RESULT_CODES.WEAK_PASSWORD -> {
                        val config = DialogConfig(
                            title = R.string.generic_error_title,
                            body = R.string.error_weak_password,
                            lottieRes = R.raw.lottie_404
                        )
                        _onError.value = Event(config)
                    }
                    RegisterUseCase.RESULT_CODES.EMAIL_ALREADY_EXISTS -> {
                        val config = DialogConfig(
                            title = R.string.generic_error_title,
                            body = R.string.error_email_already_exists,
                            lottieRes = R.raw.lottie_404
                        )
                        _onError.value = Event(config)
                    }
                    RegisterUseCase.RESULT_CODES.EMAIL_MALFORMED -> {
                        val config = DialogConfig(
                            title = R.string.generic_error_title,
                            body = R.string.error_email_malformed,
                            lottieRes = R.raw.lottie_404
                        )
                        _onError.value = Event(config)
                    }
                    RegisterUseCase.RESULT_CODES.GENERIC_ERROR -> {
                        val config = DialogConfig(
                            title = R.string.generic_error_title,
                            body = R.string.generic_error_body,
                            lottieRes = R.raw.lottie_404
                        )
                        _onError.value = Event(config)
                    }
                    RegisterUseCase.RESULT_CODES.DATABASE_ERROR -> {
                        val config = DialogConfig(
                            title = R.string.generic_error_title,
                            body = R.string.error_register_database,
                            lottieRes = R.raw.message_alert
                        )
                        _onError.value = Event(config)
                    }
                    RegisterUseCase.RESULT_CODES.SUCCESS -> {
                        analytics.onSignUp(SignUpType.FORM, _email.value.orEmpty(), courseSelected.value, city.value.orEmpty(), academicGroup.value.orEmpty(), academicRecord.value.orEmpty())
                        onSuccessRegister()
                    }
                }
            }
        }
    }

    fun setGenreSelected(index: Int) {
        genderSelected = genderOptions[index]
    }

    fun setCourseSelected(index: Int) {
        courseSelected = courseOptions[index]
    }

    fun setDateField(date: String?) {
        date?.let {
            _age.value = it
        }
    }

    fun getGenreOptions(context: Context): List<String> = genderOptions.map {
        with(context) {
            if (it == UserGender.MALE) {
                getString(R.string.gender_male)
            } else {
                getString(R.string.gender_female)
            }
        }
    }

    private fun emailValid(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    private fun passwordValid(password: String) = password.length > 6

    private fun onSuccessRegister() {
        if (CurrentUser.isEmailVerfied) {
            _onSuccessRegister.value = Event(true)
        } else {
            initVerifyEmail()
        }
    }

    private fun allValuesAreFilled(): Boolean {
        val config = DialogConfig(
            title = R.string.generic_error_title,
            body = R.string.error_email_password_malformed,
            lottieRes = R.raw.lottie_404
        )
        return when {
            _name.value?.isNullOrBlank() != false -> {
                _inputDataError.value = Event(RegisterError.EMPTY_NAME)
                false
            }
            _surname.value?.isNullOrBlank() != false -> {
                _inputDataError.value = Event(RegisterError.EMPTY_SURNAME)
                false
            }
            _academicGroup.value?.isNullOrBlank() != false -> {
                _inputDataError.value = Event(RegisterError.EMPTY_ACADEMIC_GROUP)
                false
            }
            _age.value?.isNullOrBlank() != false -> {
                _inputDataError.value = Event(RegisterError.EMPTY_AGE)
                false
            }
            _academicRecord.value?.isNullOrBlank() != false -> {
                _inputDataError.value = Event(RegisterError.EMPTY_ACADEMIC_RECORD)
                false
            }
            _city.value?.isNullOrBlank() != false -> {
                _inputDataError.value = Event(RegisterError.EMPTY_CITY)
                false
            }
            _email.value?.isNullOrBlank() != false -> {
                _onError.value = Event(config)
                false
            }
            _email.value?.let { emailValid(it) } ?: false == false -> {
                _onError.value = Event(config)
                false
            }
            _password.value?.isNullOrBlank() != false -> {
                _onError.value = Event(config)
                false
            }
            _password.value?.let { passwordValid(it) } ?: false == false -> {
                _onError.value = Event(config)
                false
            }
            else -> true
        }
    }
}