import SwiftUI

struct AppView: View {
    @State private var path: [String] = []

    var body: some View {
        NavigationStack(path: $path) {
            HomeView(path: $path)
                .toolbar(.hidden, for: .navigationBar)
                .navigationDestination(for: String.self) { code in
                    CountryDetailView(code: code)
                }
        }
    }
}

#Preview {
    AppView()
}
