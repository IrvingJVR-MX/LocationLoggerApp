package com.bootcamp.locationloggerapp.ui

import android.net.Uri
import androidx.lifecycle.Observer
import com.bootcamp.locationloggerapp.data.source.*
import com.bootcamp.locationloggerapp.m.ui.ui.post.viewmodel.PostViewModel
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
class PostViewModelTest : TestCase() {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private lateinit var viewModel: PostViewModel
    private lateinit var fakeFirebaseManager: FakeFirebaseManager
    private lateinit var fakeFirebaseStorageManager: FakeFirebaseStorageManager

    @Before
    public override fun setUp() {
        super.setUp()
        hiltRule.inject()
        fakeFirebaseManager = FakeFirebaseManager()
        fakeFirebaseStorageManager = FakeFirebaseStorageManager()
        viewModel = PostViewModel(fakeFirebaseStorageManager, fakeFirebaseManager)
    }

    @Test
    fun `Given a valid email and password user is created`() {
        runBlocking {
            val observer = Observer<Boolean> {}
            try {
                viewModel.onPosted.observeForever(observer)
                val uri =
                    Uri.parse("https://external-preview.redd.it/AmeYet2y0EBc6txwuEAYieQKj_JApXRYJtiL1L_wj6c.jpg?auto=webp&v=enabled&s=652cd422cfcfc499ddb4b45bf3a39b093b96b3ed")
                viewModel.addPost(FakeData.post, uri)
                val value = viewModel.onPosted.getOrAwaitValue()
                assertNotNull(value)
            } finally {
                viewModel.onPosted.removeObserver(observer)
            }
        }
    }
}