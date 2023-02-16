package com.bootcamp.locationloggerapp.ui

import androidx.lifecycle.Observer
import com.bootcamp.locationloggerapp.data.source.FakeFirebaseManager
import com.bootcamp.locationloggerapp.data.source.FakeFirebaseStorageManager
import com.bootcamp.locationloggerapp.data.source.FakeUserManager
import com.bootcamp.locationloggerapp.data.source.getOrAwaitValue
import com.bootcamp.locationloggerapp.m.ui.repository.model.Post
import com.bootcamp.locationloggerapp.m.ui.repository.model.User
import com.bootcamp.locationloggerapp.m.ui.ui.profile.viewmodel.ProfileViewModel
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
class ProfileViewModelTest : TestCase() {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private lateinit var viewModel: ProfileViewModel
    private lateinit var fakeUserManager: FakeUserManager
    private lateinit var fakeFirebaseManager: FakeFirebaseManager
    private lateinit var fakeFirebaseStorageManager: FakeFirebaseStorageManager

    @Before
    public override fun setUp() {
        super.setUp()
        hiltRule.inject()
        fakeUserManager = FakeUserManager()
        fakeFirebaseManager = FakeFirebaseManager()
        fakeFirebaseStorageManager = FakeFirebaseStorageManager()
        viewModel =
            ProfileViewModel(fakeUserManager, fakeFirebaseManager, fakeFirebaseStorageManager)
    }

    @Test
    fun `Given a user id it will obtain its collections`() {
        runBlocking {
            val observer = Observer<MutableList<Post>> {}
            try {
                viewModel.logPostList.observeForever(observer)
                viewModel.getUserCollections("1")
                val value = viewModel.logPostList.getOrAwaitValue()
                assertNotNull(value)
            } finally {
                viewModel.logPostList.removeObserver(observer)
            }
        }
    }

    @Test
    fun `Given a user id will obtain its info`() {
        runBlocking {
            val observer = Observer<User> {}
            try {
                viewModel.userInfo.observeForever(observer)
                viewModel.getUserInfo("1")
                val value = viewModel.userInfo.getOrAwaitValue()
                assertNotNull(value)
            } finally {
                viewModel.userInfo.removeObserver(observer)
            }
        }
    }

    @Test
    fun `User sign out`() {
        runBlocking {
            val observer = Observer<Boolean> {}
            try {
                viewModel.onUserSignOut.observeForever(observer)
                viewModel.signOut()
                val value = viewModel.onUserSignOut.getOrAwaitValue()
                assertNotNull(value)
            } finally {
                viewModel.onUserSignOut.removeObserver(observer)
            }
        }
    }
}