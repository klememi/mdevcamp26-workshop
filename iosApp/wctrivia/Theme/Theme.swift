import SwiftUI

struct Theme {
    let bg: Color
    let card: Color
    let ink: Color
    let sub: Color
    let dim: Color
    let accent: Color
    let accentInk: Color
    let heat: Color
}

extension Theme {
    static let light = Theme(
        bg: Color(hex: "F4F3EE"),
        card: .white,
        ink: Color(hex: "0C1B11"),
        sub: Color(hex: "0C1B11").opacity(0.55),
        dim: Color(hex: "0C1B11").opacity(0.12),
        accent: Color(hex: "E3FF3C"),
        accentInk: Color(hex: "0C1B11"),
        heat: Color(hex: "FF5A1F")
    )

    static let dark = Theme(
        bg: Color(hex: "0C1B11"),
        card: Color(hex: "132419"),
        ink: Color(hex: "F4F3EE"),
        sub: Color(hex: "F4F3EE").opacity(0.60),
        dim: Color(hex: "F4F3EE").opacity(0.12),
        accent: Color(hex: "E3FF3C"),
        accentInk: Color(hex: "0C1B11"),
        heat: Color(hex: "FF7A3A")
    )
}

extension Color {
    init(hex: String) {
        let hex = hex.trimmingCharacters(in: CharacterSet.alphanumerics.inverted)
        var int: UInt64 = 0
        Scanner(string: hex).scanHexInt64(&int)
        let r = Double((int >> 16) & 0xFF) / 255
        let g = Double((int >> 8) & 0xFF) / 255
        let b = Double(int & 0xFF) / 255
        self.init(red: r, green: g, blue: b)
    }
}

extension String? {
	
	func toUrl() -> URL? {
		guard let self else { return nil }
		return URL(string: self)
	}
}
