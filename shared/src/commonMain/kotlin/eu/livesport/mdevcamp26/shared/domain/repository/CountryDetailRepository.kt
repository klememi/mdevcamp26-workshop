package eu.livesport.mdevcamp26.shared.domain.repository

import eu.livesport.mdevcamp26.shared.domain.model.CountryDetail
import eu.livesport.mdevcamp26.shared.domain.repository.key.CountryDetailKey

interface CountryDetailRepository : Repository<CountryDetailKey, CountryDetail>
