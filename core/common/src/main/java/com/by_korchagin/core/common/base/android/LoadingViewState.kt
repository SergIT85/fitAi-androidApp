package com.by_korchagin.core.common.base.android

import com.by_korchagin.core.common.base.ViewState

/**
 * Base interface for states with loading indicator.
 *
 * Use for screens with async data loading.
 */
interface LoadingViewState : ViewState {
    val isLoading: Boolean
}

/**
 * Base interface for states with error handling.
 */
interface ErrorViewState : ViewState {
    val error: String?
}

/**
 * Combined interface for common state pattern.
 */
interface BaseScreenState : LoadingViewState, ErrorViewState {
    override val isLoading: Boolean
    override val error: String?
}

  /**
  * Usage example:
  * data class MyViewState(
  *     override val isLoading: Boolean = false,
  *     override val error: String? = null,
  *     val data: List<Item> = emptyList()
  * ) : BaseScreenState
  */