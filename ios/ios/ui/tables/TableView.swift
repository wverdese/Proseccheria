//
//  TableView.swift
//  ios
//
//  Created by Walt Verdese on 24/07/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct TableView: View {
    @StateObject var viewModel = TableViewModel()

    var body: some View {
        TableList(state: viewModel.state.tables) { row in
            viewModel.onRowTap(state: row)
        }
    }
}
