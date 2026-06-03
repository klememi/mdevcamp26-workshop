import SwiftUI

struct FlagBadgeView: View {
	let url: URL?
	let size: CGFloat

	var body: some View {
		AsyncImage(url: url) { phase in
			switch phase {
			case let .success(image):
				image.resizable().scaledToFill()

			default:
				Color.gray.opacity(0.3)
			}
		}
		.frame(width: size, height: size)
		.clipShape(Circle())
		.overlay(Circle().stroke(Color.black.opacity(0.12), lineWidth: 1))
	}
}
