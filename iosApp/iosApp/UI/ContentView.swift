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
        }.onAppear {
            giphyViewModel.initViewModel()
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
    
    var body: some View {
        VStack {
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
            GiphyAutoCompleteView(viewModel: viewModel, searchQuery: $searchQuery)
                .visibility(viewModel.autoCompleteListVisibility)
        }
    }
}

struct GiphyAutoCompleteView: View {
    @StateObject var viewModel: GiphyViewModel
    @Binding var searchQuery: String
    
    var body: some View {
        VStack {
            Button(action: { viewModel.setAutoCompleteVisibility(visible: false) }) {
                HStack {
                    Spacer()
                    Text("X")
                        .font(.body)
                        .bold()
                        .frame(width:40, height: 40)
                        .background(RoundedRectangle(cornerRadius: 10).fill(Color.purple))
                        .foregroundColor(.white)
                }
            }
            LazyVGrid(columns: Array(repeating: .init(.flexible()), count: 3)) {
                ForEach(viewModel.autoCompleteList, id: \.self) { uiModel in
                    Text(uiModel.name)
                        .lineLimit(1)
                        .foregroundColor(.blue)
                        .font(.body)
                        .padding(.horizontal)
                        .onTapGesture {
                            searchQuery = uiModel.name
                            viewModel.updateSearchQuery(query: uiModel.name)
                        }
                }
            }.padding()
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
                        .frame(width: 150, height: 150)
                        .background(Color(.secondarySystemBackground))
                }
            }
        }
    }
}

struct GiphyGridItem: View {
    @State private var imageData: Data? = nil
    var uiModel: GifUiModel
    
    var body: some View {
        if let data = imageData {
            GIFImage(data: data)
        } else {
            Text("Loading..")
                .onAppear {
                    loadData(url: uiModel.downsizedUrl)
                }
        }
    }
    
    private func loadData(url: String) {
        let task = URLSession.shared.dataTask(with: URL(string: url) ?? URL(fileURLWithPath: "placeHolder")) {data,_,_ in
            imageData = data
        }
        task.resume()
    }
}
