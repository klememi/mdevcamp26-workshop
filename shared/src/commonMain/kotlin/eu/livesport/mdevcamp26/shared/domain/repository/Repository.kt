package eu.livesport.mdevcamp26.shared.domain.repository

import eu.livesport.mdevcamp26.shared.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface Repository<Key, Data> {
    fun getData(key: Key): Flow<Result<Data>>
}
