import SwiftUI
import shared
import SwiftUISnackbar

struct ContentView: View {
    @StateObject var giphyViewModel: GiphyViewModel = GiphyViewModel()
    
    let greet = Greeting().greet()
    
    var body: some View {
        TabView {
            GiphyMainLayout(giphyViewModel: giphyViewModel)
                .tabItem {
                    Text("Main")
                }
            
            GiphyScrapLayout(giphyViewModel: giphyViewModel)
                .tabItem {
                    Text("Scrap")
                }
        }
    }
}

struct GiphyMainLayout: View {
    @StateObject var giphyViewModel: GiphyViewModel
    
    var body: some View {
        VStack() {
            GiphyMainTitleView(viewModel: giphyViewModel)
            GiphySearchView(viewModel: giphyViewModel)
            GiphyMainGridView(viewModel: giphyViewModel)
            Spacer()
        }.onAppear {
            giphyViewModel.initViewModel()
        }
    }
}

struct GiphyScrapLayout: View {
    @StateObject var giphyViewModel: GiphyViewModel
    
    var body: some View {
        VStack() {
            GiphyScrapTitleView(viewModel: giphyViewModel)
            GiphyScrapGridView(viewModel: giphyViewModel)
            Spacer()
        }.onAppear {
            giphyViewModel.getScrapGifs()
        }
    }
}

struct GiphyMainTitleView: View {
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

struct GiphyScrapTitleView: View {
    @StateObject var viewModel: GiphyViewModel
    
    var body: some View {
        Text(String(viewModel.scrapGifList.count) + " ScrapGifs")
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

struct GiphyMainGridView: View {
    @StateObject var viewModel: GiphyViewModel
    @State var snackbarShow: Bool = false
    
    var body: some View {
        GeometryReader {_ in
            ScrollView {
                LazyVGrid(columns: Array(repeating: .init(.flexible()), count: 3)) {
                    ForEach(viewModel.gifList, id: \.self) { uiModel in
                        GiphyGridItem(uiModel: uiModel)
                            .frame(width: 150, height: 150)
                            .background(Color(.secondarySystemBackground))
                            .onTapGesture(count: 2){
                                snackbarShow = true
                                viewModel.addScrap(uiModel: uiModel)
                            }
                    }
                }
            }
            .snackbar(isShowing: $snackbarShow, title: "Complete Scrap!", style: SnackbarStyle.default)
        }
        
    }
}

struct GiphyScrapGridView: View {
    @StateObject var viewModel: GiphyViewModel
    @State var snackbarShow: Bool = false

    var body: some View {
        ScrollView {
            LazyVGrid(columns: Array(repeating: .init(.flexible()), count: 3)) {
                ForEach(viewModel.scrapGifList, id: \.self) { uiModel in
                    GiphyGridItem(uiModel: uiModel)
                        .frame(width: 150, height: 150)
                        .background(Color(.secondarySystemBackground))
                        .onTapGesture(count: 2){
                            snackbarShow = true
                            viewModel.removeScrap(uiModel: uiModel)
                            viewModel.getScrapGifs()
                        }
                }
            }
        }.snackbar(isShowing: $snackbarShow, title: "Remove Scrap!", style: SnackbarStyle.default)
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
