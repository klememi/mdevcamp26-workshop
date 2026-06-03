package eu.livesport.mdevcamp26.domain.repository

import eu.livesport.mdevcamp26.domain.repository.model.HomeData
import eu.livesport.mdevcamp26.domain.repository.key.HomeDataKey

interface HomeDataRepository : Repository<HomeDataKey, HomeData>
