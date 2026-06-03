package eu.livesport.mdevcamp26.domain.repository

import eu.livesport.mdevcamp26.domain.repository.model.CountryDetail
import eu.livesport.mdevcamp26.domain.repository.key.CountryDetailKey

interface CountryDetailRepository : Repository<CountryDetailKey, CountryDetail>
