package com.sevenstars.domain.usecase

import com.sevenstars.domain.model.auth.RequestSignInEntity
import com.sevenstars.domain.model.auth.ResponseSignInEntity
import com.sevenstars.domain.repository.auth.AuthRepository

/*
[UseCase 사용 이유]
1. 해당 ViewModel이 어떤 것을 하고자 하는지 직관적으로 파악할 수 있다.
    이것을 "Screaming Architecture"라고 부르기도 하는데, 말 그대로 어떠한 서비스를 제공하는지 딱 봐도 알 수 있도록 하는 것이다.
    때문에 UseCase의 이름은 직관적으로 어떤 것을 수행하는지 알 수 있게 지어야 한다.
    그리고 ViewModel에서는 해당 UseCase를 파라미터로 전달받아 사용하게 되니 ViewModel에서 어떠한 일은 하는지 파라미터만 확인해도 알 수 있게 된다.
    이러한 구조는 유지/보수 측면에서도 유용하지만, 여럿이서 작업하는 협업 구조에서도 도움이 된다고 생각한다.

2. 의존성을 줄일 수 있다.
    첫번째에서 설명한 것처럼 ViewModel에서는 UseCase를 파라미터로 전달 받아서 사용하게 된다.
    UseCase를 사용하지 않게 되면 ViweModel에서는 Repository를 전달 받아서 사용하게 되는데, 전달 받은 Repository가 수정된다면
    해당 Repository를 사용하는 많은 부분에서 수정이 이루어져야 할 가능성이 높다.
    하지만, UseCaes를 사용하게 되면 영향이 있는 UseCase를 사용하는 부분에서만 수정을 하면 되기 때문에 의존성이 줄어든다.
 */

class SignInUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(body: RequestSignInEntity): Result<ResponseSignInEntity>{
        return repository.signIn(body)
    }
}