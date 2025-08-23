// pay_customer_model.dart
class PayCustomerModel {
  int? id;
  String? date;
  String? customerName;
  String? planName;
  int? price;

  PayCustomerModel({
    this.id,
    this.date,
    this.customerName,
    this.planName,
    this.price,
  });

  // Chuyển từ JSON sang model
  factory PayCustomerModel.fromJson(Map<String, dynamic> json) {
    return PayCustomerModel(
      id: json['id'] as int?,
      date: json['date'] as String?,
      customerName: json['customerName'] as String?,
      planName: json['planName'] as String?,
      price: json['price'] as int?,
    );
  }

  // Chuyển từ model sang JSON
  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'date': date,
      'customerName': customerName,
      'planName': planName,
      'price': price,
    };
  }
}
