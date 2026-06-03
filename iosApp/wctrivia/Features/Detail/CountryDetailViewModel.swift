import Foundation
import Combine

@MainActor
final class CountryDetailViewModel: ObservableObject {
	
	enum ViewState: Equatable {
		case content(CountryDetail)
		case error(String)
		case loading
	}
	
	@Published private(set) var viewState: ViewState = .loading

    func load(code: String) async {
		guard viewState == .loading else { return }
        do {
			viewState = .content(try await WCApi.detail(code: code))
        } catch {
			viewState = .error(error.localizedDescription)
        }
    }
}
