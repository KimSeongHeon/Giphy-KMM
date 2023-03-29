import SwiftUI
import Swinject
import shared

@main
struct iOSApp: App {
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
    
    var window: UIWindow?
    lazy var container: Container = {
        let builder = Container()
        builder.register(GiphySharedEngine.self) { resolver in
            GiphySharedEngine()
        }.inObjectScope(.container)
        return builder
    }()
}
