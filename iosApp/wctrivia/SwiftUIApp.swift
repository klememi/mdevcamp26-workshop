import SwiftUI
import Shared

@main
struct SwiftUIApp: App {
	
	init() {
		startKoin()
	}
	
    var body: some Scene {
        WindowGroup {
            AppView()
        }
    }
}
