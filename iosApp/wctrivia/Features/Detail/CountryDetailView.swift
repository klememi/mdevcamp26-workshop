import Shared
import SwiftUI

struct CountryDetailView: View {
	
	#warning("TODO: Declare shared viewModel. It needs parameter for initialization so you cant use @Inject directly. Use @State to store it effectively")

	@Environment(\.dismiss) private var dismiss
	@Environment(\.colorScheme) private var colorScheme

	init(code: String) {
		#warning("TODO: Initialize shared viewModel using code parameter and Inject constructor")
	}

	private var theme: Theme {
		colorScheme == .dark ? .dark : .light
	}

	var body: some View {
		ScrollView {
			#warning("TODO: Implement content UI using shared viewModel and provided main view functions")
		}
		.background(theme.bg)
		.scrollIndicators(.hidden)
		.ignoresSafeArea(edges: .top)
		.navigationBarBackButtonHidden(true)
	}

	// MARK: - Main view functions

	private func content(_ detail: CountryDetail) -> some View {
		ScrollView {
			VStack(spacing: 0) {
				heroSection(detail: detail)
				bodySection(detail: detail)
					.padding(.bottom, 48)
			}
		}
		.background(theme.bg)
		.scrollIndicators(.hidden)
	}

	private func errorView(message: String) -> some View {
		VStack(spacing: 12) {
			backButton
				.padding(.top, 58)
			Spacer()
			Text("Couldn't load country")
				.font(.system(size: 18, weight: .black))
				.foregroundStyle(theme.ink)
			Text(message)
				.font(.system(size: 13))
				.foregroundStyle(theme.sub)
				.multilineTextAlignment(.center)
			Spacer()
		}
		.padding(.horizontal, 20)
		.frame(maxWidth: .infinity, maxHeight: .infinity)
		.background(theme.bg)
	}
	
	private func progressView() -> some View {
		ProgressView()
			.tint(theme.ink)
			.frame(maxWidth: .infinity, maxHeight: .infinity)
			.background(theme.bg)
	}
	
	// MARK: - Back button
	
	private var backButton: some View {
		Button {
			dismiss()
		} label: {
			Image(systemName: "chevron.left")
				.font(.system(size: 14, weight: .semibold))
				.foregroundStyle(theme.accentInk)
				.frame(width: 40, height: 40)
				.background(Color.black.opacity(0.12))
				.clipShape(Circle())
		}
		.buttonStyle(.plain)
	}

	// MARK: - Hero

	private func heroSection(detail: CountryDetail) -> some View {
		ZStack(alignment: .topLeading) {
			theme.accent.ignoresSafeArea(edges: .top)

			VStack(alignment: .leading, spacing: 0) {
				backButton
					.padding(.top, 58)
					.padding(.bottom, 18)

				Text("Group \(detail.group)  ·  \"\(detail.nick)\"")
					.font(.system(size: 11, design: .monospaced))
					.foregroundStyle(theme.accentInk.opacity(0.7))
					.tracking(1.4)
					.textCase(.uppercase)

				Text(detail.name.uppercased())
					.font(.system(size: 54, weight: .black))
					.foregroundStyle(theme.accentInk)
					.tracking(-2)
					.lineLimit(2)
					.minimumScaleFactor(0.6)
					.padding(.top, 6)

				HStack(alignment: .top, spacing: 16) {
					heroFlag(url: detail.flagUrl.toUrl())
					HStack(spacing: 0) {
						statPair(label: "FIFA RANK", value: "#\(detail.ranking)")
						statPair(label: "APPEARANCES", value: "\(detail.appearances)×")
					}
					.frame(maxWidth: .infinity, alignment: .leading)
				}
				.padding(.top, 22)
				.padding(.bottom, 28)
			}
			.padding(.horizontal, 20)
		}
	}

	private func heroFlag(url: URL?) -> some View {
		AsyncImage(url: url) { phase in
			switch phase {
			case let .success(image):
				image.resizable().scaledToFill()
			default:
				Color.gray.opacity(0.3)
			}
		}
		.frame(width: 96, height: 96)
		.clipShape(RoundedRectangle(cornerRadius: 20))
		.overlay(RoundedRectangle(cornerRadius: 20).stroke(Color.black.opacity(0.15), lineWidth: 1))
		.shadow(color: .black.opacity(0.2), radius: 10, x: 0, y: 6)
	}

	private func statPair(label: String, value: String) -> some View {
		VStack(alignment: .leading, spacing: 2) {
			Text(label)
				.font(.system(size: 10, design: .monospaced))
				.foregroundStyle(theme.accentInk.opacity(0.7))
				.tracking(1)
			Text(value)
				.font(.system(size: 34, weight: .black))
				.foregroundStyle(theme.accentInk)
				.tracking(-1)
		}
		.frame(maxWidth: .infinity, alignment: .leading)
	}

	// MARK: - Body

	private func bodySection(detail: CountryDetail) -> some View {
		VStack(spacing: 10) {
			bestFinishCard(best: detail.best)

			HStack(spacing: 10) {
				statTile(label: "Star · 2026", value: detail.star)
				statTile(label: "All-time top scorer", value: detail.topScorer)
			}
			.fixedSize(horizontal: false, vertical: true)

			momentPanel(moment: detail.moment)
			factPanel(fact: detail.fact)
			groupmatesSection(group: detail.group, mates: detail.groupCountries)
		}
		.padding(.horizontal, 20)
		.padding(.top, 20)
	}

	private func bestFinishCard(best: String) -> some View {
		VStack(alignment: .leading, spacing: 8) {
			Text("BEST FINISH")
				.font(.system(size: 10, design: .monospaced))
				.foregroundStyle(theme.bg.opacity(0.7))
				.tracking(1.2)
			Text(best.uppercased())
				.font(.system(size: 30, weight: .black))
				.foregroundStyle(theme.bg)
				.tracking(-1)
		}
		.frame(maxWidth: .infinity, alignment: .leading)
		.padding(.horizontal, 18)
		.padding(.vertical, 16)
		.background(theme.ink)
		.clipShape(RoundedRectangle(cornerRadius: 20))
	}

	private func statTile(label: String, value: String) -> some View {
		VStack(alignment: .leading, spacing: 8) {
			Text(label.uppercased())
				.font(.system(size: 10, design: .monospaced))
				.foregroundStyle(theme.sub)
				.tracking(1.2)
			Text(value.uppercased())
				.font(.system(size: 18, weight: .black))
				.foregroundStyle(theme.ink)
				.tracking(-0.5)
				.lineLimit(3)
				.fixedSize(horizontal: false, vertical: true)
		}
		.frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topLeading)
		.padding(14)
		.cardSurface(theme: theme, cornerRadius: 18)
	}

	private func momentPanel(moment: String) -> some View {
		taggedPanel(tag: "Moment", tagColor: theme.heat, tagTextColor: .white) {
			Text(moment.uppercased())
				.font(.system(size: 22, weight: .black))
				.foregroundStyle(theme.ink)
				.tracking(-0.8)
		}
	}

	private func factPanel(fact: String) -> some View {
		taggedPanel(tag: "Did you know?", tagColor: theme.accent, tagTextColor: theme.accentInk) {
			Text(fact)
				.font(.system(size: 16))
				.foregroundStyle(theme.ink)
				.lineSpacing(4)
		}
	}

	private func taggedPanel(
		tag: String,
		tagColor: Color,
		tagTextColor: Color,
		@ViewBuilder content: () -> some View
	) -> some View {
		VStack(alignment: .leading, spacing: 10) {
			tagView(text: tag, color: tagColor, textColor: tagTextColor)
			content()
		}
		.frame(maxWidth: .infinity, alignment: .leading)
		.padding(16)
		.cardSurface(theme: theme, cornerRadius: 20)
	}

	private func groupmatesSection(group: String, mates: [Shared.GroupTeam]) -> some View {
		VStack(alignment: .leading, spacing: 8) {
			if !mates.isEmpty {
				Text("Also in Group \(group)")
					.font(.system(size: 11, design: .monospaced))
					.foregroundStyle(theme.sub)
					.tracking(1.2)
					.textCase(.uppercase)
					.padding(.leading, 4)

				ScrollView(.horizontal, showsIndicators: false) {
					HStack(spacing: 10) {
						ForEach(mates, id: \.name) { mate in
							groupmateChip(mate)
						}
					}
					.padding(.horizontal, 2)
					.padding(.bottom, 4)
				}
			}
		}
		.padding(.top, 8)
	}

	private func groupmateChip(_ mate: Shared.GroupTeam) -> some View {
		HStack(spacing: 8) {
			FlagBadgeView(url: mate.flagUrl.toUrl(), size: 26)
			Text(mate.name)
				.font(.system(size: 13, weight: .semibold))
				.foregroundStyle(theme.ink)
		}
		.padding(.leading, 8)
		.padding(.trailing, 12)
		.padding(.vertical, 8)
		.cardSurface(theme: theme, cornerRadius: 12)
	}

	private func tagView(text: String, color: Color, textColor: Color) -> some View {
		Text(text.uppercased())
			.font(.system(size: 10, weight: .semibold, design: .monospaced))
			.foregroundStyle(textColor)
			.tracking(1.2)
			.padding(.horizontal, 9)
			.padding(.vertical, 5)
			.background(color)
			.clipShape(Capsule())
	}
}

private extension View {
	func cardSurface(theme: Theme, cornerRadius: CGFloat) -> some View {
		background(theme.card)
			.overlay(RoundedRectangle(cornerRadius: cornerRadius).stroke(theme.dim, lineWidth: 1))
			.clipShape(RoundedRectangle(cornerRadius: cornerRadius))
	}
}

#Preview {
	NavigationStack {
		CountryDetailView(code: "ar")
	}
}
