//
//  Koin.swift
//  AirthingsApp
//
//  Created by Walt Verdese on 28/10/2021.
//  Copyright © 2021 Airthings. All rights reserved.
//

import Foundation
import shared

private var _koin: Koin_coreKoin?

/// Initializes Koin service locator.
///
/// This should be called exactly once from the app's main delegate, for example from [UIApplicationDelegate]'s
/// application(application: didFinishLaunchingWithOptions:) method.
func startKoin() {
    let koinApplication = KoinKt.doInitKoinIos()
    _koin = koinApplication.koin
}

/// Returns the shared [Koin][Koin_coreKoin] instance.
///
/// Be sure to call [startKoin] from the app delegate, otherwise the app would crash unceremoniously.
var koin: Koin_coreKoin {
    #if DEBUG
    guard let nonNilKoin = _koin else {
        fatalError("Koin isn't initialized yet! Did you forget to call startKoin()?")
    }
    return nonNilKoin
    #else
    return _koin!
    #endif
}
