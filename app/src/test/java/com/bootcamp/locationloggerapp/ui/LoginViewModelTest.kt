package com.bootcamp.locationloggerapp.ui

import androidx.lifecycle.Observer
import com.bootcamp.locationloggerapp.data.source.FakeUserManager
import com.bootcamp.locationloggerapp.data.source.getOrAwaitValue
import com.bootcamp.locationloggerapp.m.ui.ui.login.viewmodel.LoginViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class, manifest = Config.NONE)
@HiltAndroidTest
class LoginViewModelTest : TestCase() {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private lateinit var viewModel: LoginViewModel
    private lateinit var fakeManager: FakeUserManager

    private val email: String = "test@gmail.com"
    private var password: String = "test.test"

    @Before
    public override fun setUp() {
        super.setUp()
        hiltRule.inject()
        fakeManager = FakeUserManager()
        viewModel = LoginViewModel(fakeManager)
    }

    @Test
    fun `Given a valid email and password user is logged in`() {
        runBlocking {
            val observer = Observer<Boolean> {}
            try {
                viewModel.onLogin.observeForever(observer)
                val result = fakeManager.signInWithEmailAndPassword(email, password)
                assertEquals(result.data.toString(), "success")
                viewModel.signIn(email, password)
                val value = viewModel.onLogin.getOrAwaitValue()
                assertTrue(value)
            } finally {
                viewModel.onLogin.removeObserver(observer)
            }
        }
    }
}