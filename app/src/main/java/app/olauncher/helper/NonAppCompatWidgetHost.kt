package app.olauncher.helper

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetHostView
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.view.LayoutInflater

/**
 * Custom AppWidgetHost that creates widget views using a plain (non-AppCompat)
 * LayoutInflater. This prevents AppCompat from replacing ImageView with
 * AppCompatImageView, which breaks RemoteViews method calls.
 */
class NonAppCompatWidgetHost(context: Context, hostId: Int) : AppWidgetHost(context, hostId) {

    private val appContext = context.applicationContext

    override fun onCreateView(
        context: Context,
        appWidgetId: Int,
        appWidget: AppWidgetProviderInfo?
    ): AppWidgetHostView {
        // Use a custom AppWidgetHostView that inflates with a plain LayoutInflater
        return PlainWidgetHostView(appContext)
    }

    /**
     * AppWidgetHostView that overrides getDefaultView to use a non-AppCompat inflater.
     * The key issue is that AppWidgetHostView.updateAppWidget() calls
     * RemoteViews.apply() with the host view's context, and if that context has
     * AppCompat's LayoutInflater factory, ImageView becomes AppCompatImageView.
     *
     * By using applicationContext (which doesn't have AppCompatDelegate installed),
     * we avoid the class substitution.
     */
    private class PlainWidgetHostView(context: Context) : AppWidgetHostView(context) {
        // The applicationContext passed here doesn't have AppCompat's
        // LayoutInflater factory, so RemoteViews.apply() will inflate
        // plain android.widget.ImageView instead of AppCompatImageView.
    }
}
