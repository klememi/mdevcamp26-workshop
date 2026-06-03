import SwiftUI

struct KickoffCountdownView: View {
    let now: Date
    let kickoff: Date
    let stadium: String
    let theme: Theme

    private var diff: TimeInterval {
		max(0, kickoff.timeIntervalSince(now))
    }

    private var days: Int    { Int(diff) / 86400 }
    private var hours: Int   { (Int(diff) % 86400) / 3600 }
    private var minutes: Int { (Int(diff) % 3600) / 60 }
    private var seconds: Int { Int(diff) % 60 }

    private func pad(_ n: Int) -> String { String(format: "%02d", n) }

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            HStack(alignment: .firstTextBaseline) {
                Text("KICKOFF IN")
                    .font(.system(size: 18, weight: .black))
                    .foregroundStyle(theme.accentInk)
                    .tracking(-0.5)
                Spacer()
                Text(stadium.uppercased())
                    .font(.system(size: 10, design: .monospaced))
                    .foregroundStyle(theme.accentInk.opacity(0.7))
                    .tracking(1)
            }

            HStack(spacing: 0) {
                ForEach([pad(days), pad(hours), pad(minutes), pad(seconds)].enumerated(), id: \.offset) { (_, value) in
                    Text(value)
                        .font(.system(size: 30, weight: .medium, design: .monospaced))
                        .foregroundStyle(theme.accentInk)
                        .tracking(1)
                        .monospacedDigit()
                        .frame(maxWidth: .infinity, alignment: .leading)
                }
            }
            .padding(.top, 10)

            HStack(spacing: 0) {
                ForEach(["DAYS", "HRS", "MIN", "SEC"], id: \.self) { label in
                    Text(label)
                        .font(.system(size: 9, weight: .medium, design: .monospaced))
                        .foregroundStyle(theme.accentInk.opacity(0.55))
                        .tracking(2)
                        .frame(maxWidth: .infinity, alignment: .leading)
                }
            }
            .padding(.top, 6)
        }
        .padding(.horizontal, 18)
        .padding(.vertical, 16)
        .background(theme.accent)
        .clipShape(RoundedRectangle(cornerRadius: 20))
    }
}

extension Int64 {
	func toDate() -> Date {
		Date(timeIntervalSince1970: TimeInterval(self/1000))
	}
}
