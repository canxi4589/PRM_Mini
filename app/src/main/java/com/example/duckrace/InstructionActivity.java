package com.example.duckrace;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class InstructionActivity extends AppCompatActivity {
    private TextView instructionsText;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

        initViews();
        setupInstructions();
        setupListeners();
    }

    private void initViews() {
        instructionsText = findViewById(R.id.instructionsText);
        backButton = findViewById(R.id.backButton);
    }

    private void setupInstructions() {
        String instructions = "🏇 HƯỚNG DẪN CHƠI ĐUA VỊT 🏇  \n\n" +
                "📝 CÁCH CHƠI:\n" +
                "1. Đăng nhập vào tài khoản của bạn\n" +
                "2. Nạp tiền vào tài khoản để có thể đặt cược\n" +
                "3. Chọn 'BẮT ĐẦU ĐUA VỊT' để vào màn hình đua\n" +
                "4. Đặt cược cho các con vịt bạn nghĩ sẽ thắng\n" +
                "5. Nhấn 'BẮT ĐẦU' để bắt đầu cuộc đua\n\n" +
                "💰 CƯỢC VÀ THƯỞNG:\n" +
                "• Số tiền cược tối thiểu: 1,000 VND\n" +
                "• Tỷ lệ thưởng: x2 lần số tiền cược nếu thắng\n" +
                "• Bạn có thể đặt cược cho nhiều con vịt\n" +
                "• Chỉ con vịt về đích đầu tiên mới thắng\n\n" +
                "🎮 ĐIỀU KHIỂN:\n" +
                "• 'BẮT ĐẦU': Bắt đầu cuộc đua\n" +
                "• 'RESET': Đặt lại cuộc đua và xóa tất cả cược\n" +
                "• 'ĐẶT CƯỢC': Đặt cược cho con ngựa đã chọn\n\n" +
                "💡 MẸO CHƠI:\n" +
                "• Quản lý số tiền cẩn thận\n" +
                "• Đừng đặt cược quá nhiều trong một lần\n" +
                "• Mỗi con ngựa đều có cơ hội thắng như nhau\n" +
                "• Hãy chơi có trách nhiệm!\n\n" +
                "🎵 ÂM THANH:\n" +
                "• Nhạc nền sẽ phát trong game\n" +
                "• Có hiệu ứng âm thanh khi đua và kết thúc\n\n" +
                "Chúc bạn chơi vui vẻ và may mắn! 🍀";

        instructionsText.setText(instructions);
    }

    private void setupListeners() {
        backButton.setOnClickListener(v -> finish());
    }
}