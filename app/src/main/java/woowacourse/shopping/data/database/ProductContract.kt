package woowacourse.shopping.data.database

import android.provider.BaseColumns

object ProductContract {
    const val DATABASE_NAME = "product.db"
    const val DATABASE_VERSION = 1

    object RecentlyViewedProductEntry : BaseColumns {
        const val TABLE_NAME = "recently_viewed_product"
        const val COLUMN_NAME_PRODUCT_ID = "product_id"
        const val COLUMN_NAME_VIEWED_TIME = "viewed_time"
    }
}
