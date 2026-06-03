package eu.livesport.mdevcamp26.shared.domain.repository

import eu.livesport.mdevcamp26.shared.domain.model.HomeData
import eu.livesport.mdevcamp26.shared.domain.repository.key.HomeDataKey

interface HomeDataRepository : Repository<HomeDataKey, HomeData>
