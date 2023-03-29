import SwiftUI
import shared

struct ContentView: View {
    @StateObject var giphyViewModel: GiphyViewModel = GiphyViewModel()
    
	let greet = Greeting().greet()

	var body: some View {
        VStack() {
            GiphyTitleView(viewModel: giphyViewModel)
            GiphySearchView(viewModel: giphyViewModel)
            GiphyGridView(viewModel: giphyViewModel)
            Spacer()
        }
    }
}

struct GiphyTitleView: View {
    @StateObject var viewModel: GiphyViewModel
    
    var body: some View {
        Text(viewModel.giphyTitle)
            .lineLimit(1)
            .multilineTextAlignment(.center)
            .font(.title)
            .foregroundColor(.black)
            .padding()
    }
}

struct GiphySearchView: View {
    @StateObject var viewModel: GiphyViewModel
    @State var searchQuery: String = ""
    @State var autoCompleteList: [GifAutoCompleteUiModel] = []

    var body: some View {
        HStack {
            TextField("Search", text: $searchQuery)
                .onChange(of: searchQuery, perform: { newQuery in
                    viewModel.updateTitle(query: newQuery)
                    viewModel.updateSearchQuery(query: newQuery)
                })
                .frame(height: 50, alignment: .leading)
                .background(Color(.secondarySystemBackground))
                .padding(.leading, 5)
            
            Button(action: { viewModel.swapSearchMode() } , label: {
                Text(viewModel.searchMode.rawValue)
                    .fixedSize()
                    .frame(width: 100, height: 50, alignment: .center)
                    .foregroundColor(.white)
                    .background(Color.purple)
            })
        }
    }
}

struct GiphyGridView: View {
    @StateObject var viewModel: GiphyViewModel

    var body: some View {
        ScrollView {
            LazyVGrid(columns: Array(repeating: .init(.flexible()), count: 3)) {
                ForEach(viewModel.gifList, id: \.self) { uiModel in
                    GiphyGridItem(uiModel: uiModel)
                }
            }
        }
    }
}

struct GiphyGridItem: View {
    var uiModel: GifUiModel
    
    var body: some View {
        GIFImage(uiModel.url)
            .frame(height: 150)
    }
}
