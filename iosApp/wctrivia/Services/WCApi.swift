import Foundation

enum WCApi {
	
	enum Error: Swift.Error {
		case invalidURL
	}
	
	private static let baseURLComponents = {
		var components = URLComponents()
		components.scheme = "https"
		components.host = "workshop-backend-smoky.vercel.app"
		components.path = "/api"
		return components
	}()

    private static let decoder: JSONDecoder = {
        let d = JSONDecoder()
        d.dateDecodingStrategy = .iso8601
        return d
    }()

    static func main() async throws -> HomeData {
		var urlComponents = baseURLComponents
		urlComponents.path.append("/main")
		guard let url = urlComponents.url else {
			throw Error.invalidURL
		}
        let (data, _) = try await URLSession.shared.data(from: url)
        return try decoder.decode(HomeData.self, from: data)
    }

    static func detail(code: String) async throws -> CountryDetail {
		var urlComponents = baseURLComponents
		urlComponents.path.append("/detail")
		urlComponents.queryItems = [
			URLQueryItem(name: "code", value: code)
		]
		guard let url = urlComponents.url else {
			throw Error.invalidURL
		}
        
        let (data, _) = try await URLSession.shared.data(from: url)
        return try decoder.decode(CountryDetail.self, from: data)
    }
}
