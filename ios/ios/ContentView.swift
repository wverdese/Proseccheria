import SwiftUI
import shared

struct ContentView: View {
    let table = TableKt.firstTable()

	var body: some View {
        Text(table.name)
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
