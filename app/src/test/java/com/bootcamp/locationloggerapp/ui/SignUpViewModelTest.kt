package com.bootcamp.locationloggerapp.ui

import android.net.Uri
import com.bootcamp.locationloggerapp.data.source.FakeFirebaseStorageManager
import com.bootcamp.locationloggerapp.data.source.FakeUserManager
import com.bootcamp.locationloggerapp.m.ui.ui.signin.viewmodel.SignUpViewModel
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
import androidx.lifecycle.Observer
import com.bootcamp.locationloggerapp.data.source.getOrAwaitValue

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class, manifest = Config.NONE)
@HiltAndroidTest
class SignUpViewModelTest : TestCase() {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private lateinit var viewModel: SignUpViewModel
    private lateinit var fakeManager: FakeUserManager
    private lateinit var fakeFirebaseStorageManager: FakeFirebaseStorageManager

    private val email: String = "test@gmail.com"
    private var password: String = "test.test"

    @Before
    public override fun setUp() {
        super.setUp()
        hiltRule.inject()
        fakeManager = FakeUserManager()
        fakeFirebaseStorageManager = FakeFirebaseStorageManager()
        viewModel = SignUpViewModel(fakeManager, fakeFirebaseStorageManager)
    }

    @Test
    fun `Given a valid email and password user is created`() {
        runBlocking {
            val observer = Observer<Boolean> {}
            try {
                viewModel.onUserCreated.observeForever(observer)
                val result = fakeManager.createUserAuth(email, password)
                assertEquals(result.data.toString(), "success")
                val uri =
                    Uri.parse("https://external-preview.redd.it/AmeYet2y0EBc6txwuEAYieQKj_JApXRYJtiL1L_wj6c.jpg?auto=webp&v=enabled&s=652cd422cfcfc499ddb4b45bf3a39b093b96b3ed")
                viewModel.registerUserAuth("irving", email, password, uri)
                val value = viewModel.onUserCreated.getOrAwaitValue()
                assertTrue(value)
            } finally {
                viewModel.onUserCreated.removeObserver(observer)
            }
        }
    }
}

