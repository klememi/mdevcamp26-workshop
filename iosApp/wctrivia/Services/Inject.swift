import Shared
import SwiftUI

@propertyWrapper
struct Inject<T: AnyObject>: DynamicProperty {

	let wrappedValue: T

	init(parameters: [Any]? = nil) {
		self.wrappedValue = KoinSwiftBridge.shared.get(
			objCClass: T.self,
			parameters: parameters
		) as! T
	}
}
