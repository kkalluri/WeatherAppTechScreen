package com.example.weatherapptechscreen

import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import kotlin.jvm.Throws

@ExperimentalCoroutinesApi
class TestCoroutineRule : TestRule {
    val testCoroutineDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
    val testCoroutineScope: TestCoroutineScope = TestCoroutineScope(testCoroutineDispatcher)

    override fun apply(base: Statement, description: Description) = object : Statement() {
        @Throws(Throwable::class)
        override fun evaluate() {
            Dispatchers.setMain(testCoroutineDispatcher)
            base.evaluate()
            Dispatchers.resetMain()
            testCoroutineScope.cleanupTestCoroutines()
        }
    }

    fun runBlockingTest(block: suspend TestCoroutineScope.() -> Unit) =
        testCoroutineScope.runBlockingTest { block() }

    suspend fun methodToBeTested(block: () -> Unit) = testCoroutineScope.runBlockingTest {
        val method = async {
            block()
        }
        method.await()
    }
}
