package com.bootcamp.locationloggerapp.ui

import androidx.lifecycle.Observer
import com.bootcamp.locationloggerapp.data.source.FakeFirebaseManager
import com.bootcamp.locationloggerapp.data.source.getOrAwaitValue
import com.bootcamp.locationloggerapp.m.ui.repository.model.PostDetail
import com.bootcamp.locationloggerapp.m.ui.ui.home.viewmodel.HomeViewModel
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
class HomeViewModelTest : TestCase() {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private lateinit var viewModel: HomeViewModel
    private lateinit var fakeManager: FakeFirebaseManager

    @Before
    public override fun setUp() {
        super.setUp()
        hiltRule.inject()
        fakeManager = FakeFirebaseManager()
        viewModel = HomeViewModel(fakeManager)
    }

    @Test
    fun `Get users`() {
        runBlocking {
            val observer = Observer<MutableList<PostDetail>> {}
            try {
                viewModel.logPostList.observeForever(observer)
                viewModel.getUser()
                val value = viewModel.logPostList.getOrAwaitValue()
                assertNotNull(value)
            } finally {
                viewModel.logPostList.removeObserver(observer)
            }
        }
    }
}