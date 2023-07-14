package com.example.dragonballfinal

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class MainActivityViewModelTest {

    private val viewModel = MainActivityViewModel()

    @Test
    fun `test user valid`() {
        assertTrue(viewModel.isUserValid("123@."))
        assertTrue(viewModel.isUserValid("@."))
        assertTrue(viewModel.isUserValid(".@"))
        assertFalse(viewModel.isUserValid("123@"))
        assertFalse(viewModel.isUserValid("1323."))
    }
}