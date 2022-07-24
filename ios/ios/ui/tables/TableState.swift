//
//  TablesState.swift
//  ios
//
//  Created by Walt Verdese on 24/07/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation

extension TableView {
    struct State {
        let tables: TableList.State

        init(tables: TableList.State = TableList.State()) {
            self.tables = tables
        }
    }
}

extension TableList {
    struct State {
        let rows: [TableRow.State]

        init(rows: [TableRow.State] = []) {
            self.rows = rows
        }
    }
}

extension TableRow {
    struct State: Identifiable {
        var id: String
        let text: String
        let isAccented: Bool
    }
}
