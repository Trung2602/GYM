import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../models/AccountProvider.dart';
import '../models/PayCustomer.dart';
import '../models/Account.dart';
import '../api.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;

class PayCustomerScreen extends StatefulWidget {
  const PayCustomerScreen({super.key});

  @override
  State<PayCustomerScreen> createState() => _PayCustomerScreenState();
}

class _PayCustomerScreenState extends State<PayCustomerScreen> {
  List<PayCustomerModel> payList = [];
  bool loading = true;

  Account? account;

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    account = Provider.of<AccountProvider>(context).account;

    fetchPayCustomers();
  }

  Future<void> fetchPayCustomers() async {
    setState(() {
      loading = true;
    });

    try {
      final url = Api.getPayCustomersAll(account!.id);
      final response = await http.get(Uri.parse(url));

      if (response.statusCode == 200) {
        final List<dynamic> data = jsonDecode(response.body);
        payList = data.map((e) => PayCustomerModel.fromJson(e)).toList();
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Lỗi khi tải dữ liệu: ${response.statusCode}')),
        );
      }
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Lỗi: $e')),
      );
    }

    setState(() {
      loading = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFF0F123A),
      appBar: AppBar(
        title: const Text(
          "Thanh Toán",
          style: TextStyle(color: Colors.white),
        ),
        backgroundColor: const Color(0xFF1A237E),
        iconTheme: const IconThemeData(color: Colors.white),
      ),
      body: Padding(
        padding: const EdgeInsets.all(20.0),
        child: loading
            ? const Center(child: CircularProgressIndicator())
            : payList.isEmpty
            ? const Center(
          child: Text(
            'Chưa có thanh toán nào',
            style: TextStyle(color: Colors.white),
          ),
        )
            : ListView.builder(
          itemCount: payList.length,
          itemBuilder: (context, index) {
            final pay = payList[index];
            return Card(
              color: Colors.white.withOpacity(0.08),
              shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(15)),
              child: Padding(
                padding: const EdgeInsets.all(15.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      "Gói tập: ${pay.planName}",
                      style: const TextStyle(
                          color: Colors.white, fontSize: 16),
                    ),
                    const SizedBox(height: 5),
                    Text(
                      "Giá: ${pay.price} VND",
                      style: const TextStyle(
                          color: Colors.white70, fontSize: 14),
                    ),
                    const SizedBox(height: 5),
                    Text(
                      "Ngày đóng: ${pay.date}",
                      style: const TextStyle(
                          color: Colors.white70, fontSize: 14),
                    ),
                  ],
                ),
              ),
            );
          },
        ),
      ),
    );
  }
}
