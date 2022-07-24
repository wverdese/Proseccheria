//
//  TableList.swift
//  ios
//
//  Created by Walt Verdese on 24/07/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct TableList: View {
    var state: TableList.State

    var body: some View {
        List(state.rows) { row in
            TableRow(state: row)
        }
    }
}

struct TableList_Previews: PreviewProvider {
    static var previews: some View {
        TableList(state: TableList.State(rows: [
            TableRow.State(id: "1", text: "Table 1", isAccented: false),
            TableRow.State(id: "2", text: "Table 2", isAccented: true)
        ]))
    }
}
