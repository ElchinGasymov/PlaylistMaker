package com.example.playlistmaker.new_playlist.ui.view_model

sealed class ScreenState(
    val createBtnState: BtnCreateState = BtnCreateState.ENABLED
) {
    class Empty(
        createBtnState: BtnCreateState = BtnCreateState.DISABLED,
    ) : ScreenState(createBtnState)

    class HasContent(
        createBtnState: BtnCreateState = BtnCreateState.ENABLED,
    ) : ScreenState(createBtnState)

    data object NeedsToAsk : ScreenState()

    data object AllowedToGoOut : ScreenState()
}