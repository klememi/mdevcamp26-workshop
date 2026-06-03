import Shared
import SwiftUI

struct HomeView: View {

	@Binding
	var path: [String]

	@StateObject
	private var viewModel = HomeViewModel()

	@Environment(\.colorScheme)
	private var colorScheme

	@State
	private var searchQuery = ""

	private var theme: Theme {
		colorScheme == .dark ? .dark : .light
	}

	var body: some View {
		ScrollView {
			progressView()
		}
		.background(theme.bg)
		.scrollIndicators(.hidden)
		.task { await viewModel.load() }
	}

	// MARK: - Main view functions

	private func content(_ data: HomeData) -> some View {
		VStack(alignment: .leading, spacing: 0) {
			header(header: data.header, title: data.title, subtitle: data.subtitle)
			searchBar(
				query: searchQuery,
				visibleCount: filteredGroups(from: data).reduce(0) { $0 + $1.countries.count }
			)
			groups([])
			countdownSection(kickoff: data.kickoff, stadium: data.stadium)
				.padding(.bottom, 48)
		}
		.padding(.horizontal, 20)
	}

	private func errorView(message: String) -> some View {
		VStack(spacing: 12) {
			Text("Couldn't load the draw")
				.font(.system(size: 18, weight: .black))
				.foregroundStyle(theme.ink)
			Text(message)
				.font(.system(size: 13))
				.foregroundStyle(theme.sub)
				.multilineTextAlignment(.center)
		}
		.padding(24)
		.frame(maxWidth: .infinity, minHeight: 400)
	}

	private func progressView() -> some View {
		ProgressView()
			.tint(theme.ink)
			.frame(maxWidth: .infinity, minHeight: 600)
	}

	// MARK: - Masthead

	private func header(
		header: String,
		title: String,
		subtitle: String,
	) -> some View {
		VStack(alignment: .leading, spacing: 0) {
			Text(header.uppercased())
				.font(.system(size: 11, design: .monospaced))
				.foregroundStyle(theme.sub)
				.tracking(1.4)
				.frame(maxWidth: .infinity, alignment: .leading)
				.padding(.bottom, 22)

			Text("\(Text(title.uppercased()).foregroundStyle(theme.ink))\(Text(".").foregroundStyle(theme.heat))")
				.font(.system(size: 56, weight: .black))
				.tracking(-2)
				.lineLimit(2)
				.minimumScaleFactor(0.5)

			Text(subtitle)
				.font(.system(size: 15))
				.foregroundStyle(theme.sub)
				.lineSpacing(4)
				.padding(.top, 14)
		}
		.padding(.top, 16)
		.padding(.bottom, 22)
	}

	private func searchBar(query: String, visibleCount: Int) -> some View {
		HStack(spacing: 10) {
			Image(systemName: "magnifyingglass")
				.font(.system(size: 14))
				.foregroundStyle(theme.sub)
			TextField("Search country", text: $searchQuery)
				.font(.system(size: 15))
				.foregroundStyle(theme.ink)
				.tint(theme.accent)
			Text("\(visibleCount)/48")
				.font(.system(size: 11, design: .monospaced))
				.foregroundStyle(theme.sub)
				.tracking(1)
		}
		.padding(.horizontal, 14)
		.padding(.vertical, 12)
		.background(theme.card)
		.overlay(RoundedRectangle(cornerRadius: 14).stroke(theme.dim, lineWidth: 1))
		.clipShape(RoundedRectangle(cornerRadius: 14))
		.padding(.bottom, 22)
	}

	// MARK: - Groups

	private func groups(_ groups: [WcGroup]) -> some View {
		VStack(spacing: 12) {
			ForEach(groups, id: \.name) { group in
				GroupCardView(group: group, theme: theme) { country in
					path.append(country.code)
				}
			}
		}
	}

	// MARK: - Countdown

	private func countdownSection(kickoff: Date, stadium: String) -> some View {
		TimelineView(.periodic(from: .now, by: 1)) { context in
			KickoffCountdownView(now: context.date, kickoff: kickoff, stadium: stadium, theme: theme)
		}
		.padding(.top, 28)
	}

	private func filteredGroups(from response: HomeData) -> [CountryGroup] {
		guard !searchQuery.isEmpty else {
			return response.groups
		}

		let q = searchQuery.lowercased()
		return response.groups.compactMap { group in
			let filtered = group.countries.filter { $0.name.lowercased().contains(q) }
			return filtered.isEmpty ? nil : CountryGroup(name: group.name, countries: filtered)
		}
	}
}

// MARK: - Group Card

private struct GroupCardView: View {
	let group: WcGroup
	let theme: Theme
	let onTap: (Shared.Country) -> Void

	var body: some View {
		VStack(alignment: .leading, spacing: 14) {
			Text("\(Text("GROUP ").foregroundStyle(theme.ink))\(Text(group.name).foregroundStyle(theme.heat))")
				.font(.system(size: 34, weight: .black))
				.tracking(-1.5)
				.padding(.horizontal, 2)

			let rows = group.countries.chunked(into: 2)
			VStack(spacing: 8) {
				ForEach(rows, id: \.self) { row in
					HStack(spacing: 8) {
						ForEach(row, id: \.code) { country in
							CountryTileView(country: country, theme: theme)
								.frame(maxWidth: .infinity)
								.contentShape(Rectangle())
								.onTapGesture { onTap(country) }
						}
					}
				}
			}
		}
		.padding(16)
		.background(theme.card)
		.overlay(RoundedRectangle(cornerRadius: 22).stroke(theme.dim, lineWidth: 1))
		.clipShape(RoundedRectangle(cornerRadius: 22))
	}
}

// MARK: - Country Tile

private struct CountryTileView: View {
	let country: Shared.Country
	let theme: Theme

	var body: some View {
		HStack(spacing: 10) {
			FlagBadgeView(url: country.flagUrl.toUrl(), size: 34)
			VStack(alignment: .leading, spacing: 2) {
				Text(country.name)
					.font(.system(size: 13.5, weight: .bold))
					.foregroundStyle(theme.ink)
					.lineLimit(1)
				Text("#\(String(format: "%d", country.ranking)) · \(country.appearances)×")
					.font(.system(size: 10, design: .monospaced))
					.foregroundStyle(theme.sub)
					.tracking(0.5)
			}
			Spacer(minLength: 0)
		}
		.padding(.horizontal, 10)
		.padding(.vertical, 10)
		.background(.clear)
		.overlay(RoundedRectangle(cornerRadius: 14).stroke(theme.dim, lineWidth: 1))
		.clipShape(RoundedRectangle(cornerRadius: 14))
	}
}

// MARK: - Array chunking helper

private extension Array {
	func chunked(into size: Int) -> [[Element]] {
		stride(from: 0, to: count, by: size).map {
			Array(self[$0 ..< Swift.min($0 + size, count)])
		}
	}
}

#Preview {
	@Previewable @State
	var path: [String] = []
	return HomeView(path: $path)
}
