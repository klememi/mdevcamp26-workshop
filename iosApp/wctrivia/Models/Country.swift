import Foundation

struct Country: Decodable, Hashable, Identifiable {
    var id: String { code }
    let code: String
    let name: String
    let ranking: Int
    let appearances: Int
    let flagUrl: URL?
}

struct CountryGroup: Decodable, Hashable, Identifiable {
    var id: String { name }
    let name: String
    let countries: [Country]
}

struct HomeData: Decodable, Equatable {
    let header: String
    let title: String
    let subtitle: String
    let kickoff: Date
    let stadium: String
    let groups: [CountryGroup]
}

struct GroupTeam: Decodable, Equatable, Hashable, Identifiable {
    var id: String { name }
    let name: String
    let flagUrl: URL?
}

struct CountryDetail: Decodable, Equatable {
    let group: String
    let code: String
    let name: String
    let nick: String
    let appearances: Int
    let best: String
    let ranking: Int
    let topScorer: String
    let star: String
    let moment: String
    let fact: String
    let groupCountries: [GroupTeam]
    let flagUrl: URL?
}
