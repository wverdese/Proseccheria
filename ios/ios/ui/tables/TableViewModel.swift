//
//  TableViewModel.swift
//  ios
//
//  Created by Walt Verdese on 24/07/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared

class TableViewModel: ObservableObject {
    @Published var state: TableView.State

    private var job: Cancellable?

    private let repo = koin.getTableDataRepository()

    private var tableData: TableData? // TODO delete me

    init(state: TableView.State = TableView.State()) {
        self.state = state

        job?.cancel()
        job = repo.observeTableDataIos { tableData in
            self.tableData = tableData // TODO delete me
            self.state = TableView.State(
                tables: TableList.State(
                    rows: tableData.tables.map { tableItem in
                        TableRow.State(id: tableItem.table.id, text: tableItem.table.name, isAccented: tableItem.hasStoredData)
                    }
                )
            )
        }
    }

    func onRowTap(state: TableRow.State) {
        repo.selectTable(tableId: state.id)
        // TODO delete me
        repo.incrementQuantityIos(tableId: state.id, item: tableData!.items[0])
    }

    deinit {
        job?.cancel()
    }
}
