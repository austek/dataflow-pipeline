const baseTopicUrl = process.env.REACT_APP_PULSAR_SERVICE_URL  + '/ws/v2/consumer/persistent/showcase/';
export const topics = [
  {
    title: 'Order',
    uiUrl: 'order',
    url: baseTopicUrl + 'order/order.new.v1/order-sub',
    columnDefs: [
      { field: 'id' },
      { field: 'data.orderId' },
      { field: 'data.userId' },
      { field: 'data.walletId' },
      { field: 'data.createdOn' },
      { field: 'data.items.0.itemId' },
      { field: 'data.items.0.quantity' },
    ],
  },
  {
    title: 'Stock Success',
    uiUrl: 'stock-s',
    url: baseTopicUrl + 'stock/stock.success.v1/stock.success-sub',
    columnDefs: [
      { field: 'id' },
      { field: 'data.orderId' },
      { field: 'data.userId' },
      { field: 'data.walletId' },
      { field: 'data.items.0.itemId' },
      { field: 'data.items.0.quantity' },
    ],
  },
  {
    title: 'Stock Failure',
    uiUrl: 'stock-f',
    url: baseTopicUrl + 'stock/stock.failure.v1/stock.failure-sub',
    columnDefs: [
      { field: 'id' },
      { field: 'data.orderId' },
      { field: 'data.error' },
      { field: 'data.timestamp' },
    ],
  },
  {
    title: 'Payment Success',
    uiUrl: 'payment-s',
    url: baseTopicUrl + 'payment/payment.success.v1/payment.success-sub',
    columnDefs: [
      { field: 'id' },
      { field: 'data.orderId' },
      { field: 'data.paymentId' },
      { field: 'data.completedAt' },
      { field: 'data.amount' },
    ],
  },
  {
    title: 'Payment Failure',
    uiUrl: 'Payment-f',
    url: baseTopicUrl + 'payment/payment.failure.v1/payment.failure-sub',
    columnDefs: [
      { field: 'id' },
      { field: 'data.orderId' },
      { field: 'data.paymentId' },
      { field: 'data.error' },
      { field: 'data.timestamp' },
      { field: 'data.amount' },
    ],
  },
];
