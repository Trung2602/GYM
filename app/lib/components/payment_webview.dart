import 'package:flutter/material.dart';
import 'package:webview_flutter/webview_flutter.dart';
import 'pay_customer.dart';

class PaymentWebView extends StatefulWidget {
  final String paymentUrl;
  const PaymentWebView({super.key, required this.paymentUrl});

  @override
  State<PaymentWebView> createState() => _PaymentWebViewState();
}

class _PaymentWebViewState extends State<PaymentWebView> {
  late final WebViewController _controller;

  @override
  void initState() {
    super.initState();
    _controller = WebViewController()
      ..setJavaScriptMode(JavaScriptMode.unrestricted)
      ..setNavigationDelegate(
        NavigationDelegate(
          onNavigationRequest: (NavigationRequest request) {
            if (request.url.contains("/api/payment/return")) {
              // Lấy query params từ URL
              final uri = Uri.parse(request.url);
              final status = uri.queryParameters['status'];

              // Trả dữ liệu về màn trước
              Navigator.pop(context, {
                'status': status,
              });

              return NavigationDecision.prevent;
            }
            return NavigationDecision.navigate;
          },
        ),
      )
      ..loadRequest(Uri.parse(widget.paymentUrl));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("Thanh toán VNPAY")),
      body: WebViewWidget(controller: _controller),
    );
  }
}
