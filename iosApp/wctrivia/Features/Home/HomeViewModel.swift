import Foundation
import Combine

@MainActor
final class HomeViewModel: ObservableObject {
	
	enum ViewState: Equatable {
		case content(HomeData)
		case error(String)
		case loading
	}
	
	@Published private(set) var viewState: ViewState = .loading

    func load() async {
		guard viewState == .loading else { return }
        do {
			viewState = .content(try await WCApi.main())
        } catch {
			viewState = .error(error.localizedDescription)
        }
    }

    func reload() async {
		viewState = .loading
        await load()
    }
}
