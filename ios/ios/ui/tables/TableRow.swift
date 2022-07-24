//
//  TableRow.swift
//  ios
//
//  Created by Walt Verdese on 24/07/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct TableRow: View {
    var state: State

    var body: some View {
        HStack {
            Text(state.text)
                .padding(20)
                .foregroundColor(state.isAccented ? .accentColor : .textColor)
            Spacer()
        }
    }
}

struct TableRow_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            TableRow(state: TableRow.State(id: "1", text: "Table 1", isAccented: false))
            TableRow(state: TableRow.State(id: "2", text: "Table 2", isAccented: true))
        }
    }
}
