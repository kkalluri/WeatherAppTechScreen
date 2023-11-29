import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.weatherapptechscreen.TestCoroutineRule
import com.example.weatherapptechscreen.features.WeatherViewModel
import com.example.weatherapptechscreen.data.model.WeatherResponse
import com.example.weatherapptechscreen.data.remote.NetworkResult
import com.example.weatherapptechscreen.repository.WeatherRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class WeatherViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Use the custom TestCoroutineRule
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var mockRepository: WeatherRepositoryImpl

    @Mock
    private lateinit var cityObserver: Observer<String>

    @Mock
    private lateinit var latLongObserver: Observer<String>

    @Mock
    private lateinit var lastSearchedCityLoadedObserver: Observer<Boolean>

    @Mock
    private lateinit var weatherResponseObserver: FlowCollector<NetworkResult<WeatherResponse>>

    private lateinit var viewModel: WeatherViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel = WeatherViewModel(mockRepository)
    }

    @Test
    fun `updateEnteredCity sets enteredCity LiveData`() = testCoroutineRule.runBlockingTest {
        // Arrange
        val cityName = "TestCity"

        // Act
        viewModel.enteredCity.observeForever(cityObserver)
        viewModel.updateEnteredCity(cityName)

        // Assert
        verify(cityObserver).onChanged(cityName)
    }

    @Test
    fun `updateLatLong sets latLong LiveData`() = testCoroutineRule.runBlockingTest {
        // Arrange
        val latLong = "1.234,5.678"

        // Act
        viewModel.latLong.observeForever(latLongObserver)
        viewModel.updateLatLong(latLong)

        // Assert
        verify(latLongObserver).onChanged(latLong)
    }

    @Test
    fun `getLastSearchedCity updates LiveData and loaded status`() = testCoroutineRule.runBlockingTest {
        // Arrange
        `when`(runBlocking { mockRepository.getLastSearchCity() }).thenReturn("LastSearchedCity")

        // Act
        viewModel.enteredCity.observeForever(cityObserver)
        viewModel.lastSearchedCityLoaded.observeForever(lastSearchedCityLoadedObserver)
        viewModel.getLastSearchedCity()

        // Assert
        verify(cityObserver).onChanged("LastSearchedCity")
        verify(lastSearchedCityLoadedObserver).onChanged(true)
    }

    @Test
    fun `getWeatherByCity updates weatherResponse LiveData`() = testCoroutineRule.runBlockingTest {
        // Arrange
        val city = "TestCity"
        val mockWeatherResponse = mock(WeatherResponse::class.java)
        `when`(runBlocking { mockRepository.getWeatherByCityName(city) }).thenReturn(flow {
            emit(NetworkResult.Success(mockWeatherResponse))
        })

        // Act
        val job = launch {
            viewModel.weatherResponse.collect(weatherResponseObserver)
        }
        viewModel.getWeatherByCity(city)
        job.join()

        // Assert
        verify(weatherResponseObserver).emit(NetworkResult.Success(mockWeatherResponse))
        verifyNoMoreInteractions(weatherResponseObserver)

    }

    @Test
    fun `getWeatherByCoordinates updates weatherResponse LiveData`() = testCoroutineRule.runBlockingTest {
        // Arrange
        val lat = 1.234
        val lon = 5.678
        val mockWeatherResponse = mock(WeatherResponse::class.java)
        `when`(runBlocking { mockRepository.getWeatherByCoordinates(lat, lon) }).thenReturn(flow {
            emit(NetworkResult.Success(mockWeatherResponse))
        })

        // Act
        val job = launch {
            viewModel.weatherResponse.collect(weatherResponseObserver)
        }
        viewModel.getWeatherByCoordinates(lat, lon)
        job.join()


        // Assert
        verify(weatherResponseObserver).emit(NetworkResult.Success(mockWeatherResponse))
        verifyNoMoreInteractions(weatherResponseObserver)
    }

    @Test
    fun `saveLastSearchedCity calls repository method`() = testCoroutineRule.runBlockingTest {
        // Arrange
        val city = "TestCity"

        // Act
        viewModel.saveLastSearchedCity(city)

        // Assert
        verify(mockRepository).saveLastSearchCity(city)
    }
    @After
    fun tearDown() {
        Mockito.validateMockitoUsage()
    }

}
